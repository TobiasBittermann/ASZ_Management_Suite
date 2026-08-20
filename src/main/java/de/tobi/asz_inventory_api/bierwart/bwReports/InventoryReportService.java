package de.tobi.asz_inventory_api.bierwart.bwReports;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import de.tobi.asz_inventory_api.bierwart.drink.Drink;
import de.tobi.asz_inventory_api.bierwart.drink.DrinkService;
import de.tobi.asz_inventory_api.bierwart.inventory.Inventory;
import de.tobi.asz_inventory_api.bierwart.inventory.InventoryService;
import de.tobi.asz_inventory_api.bierwart.inventoryEntry.InventoryEntry;
import de.tobi.asz_inventory_api.bierwart.inventoryEntry.InventoryEntryService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class InventoryReportService {
    private final InventoryEntryService entryService;
    private final DrinkService drinkService;
    private final InventoryService inventoryService;

    public InventoryReportService(InventoryEntryService entryService,
                                  DrinkService drinkService, InventoryService inventoryService) {
        this.entryService = entryService;
        this.drinkService = drinkService;
        this.inventoryService = inventoryService;
    }

    private List<InventoryEntry> loadSortedEntries(long inventoryId) throws IOException {
        List<InventoryEntry> entries = entryService.getAllInventoryEntries().stream().filter(e -> e.getInventoryId() == inventoryId).toList();
        List<Drink> drinks = drinkService.getAllDrinks();

        List<InventoryEntry> sortedEntries = entries.stream()
                .sorted(Comparator
                        .comparing(e -> drinks
                                .stream()
                                .filter(d -> d.getId() == e.getDrinkId())
                                .findAny()
                                .orElseThrow()
                                .getName()
                        )).toList();

        return sortedEntries;
    }

    private BigDecimal getTotalShrinkage(long inventoryId) throws IOException {
        List<InventoryEntry> entries = loadSortedEntries(inventoryId);

        BigDecimal total = BigDecimal.ZERO;

        for (InventoryEntry entry : entries){
            total = total.add(entry.getShrinkageValue());
        }

        return total;
    }

    public byte[] generateInventoryReport(long inventoryId) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Inventory inventory = inventoryService.getAllInventories().stream().filter(i -> i.getId() == inventoryId).findAny().orElseThrow();
        BigDecimal totalShrinkage = getTotalShrinkage(inventoryId);

        //Fonts
        Font titleFont = new Font(Font.HELVETICA, 15, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 10);
        Font disclaimer = new Font(Font.HELVETICA, 7);

        //Texts
        String dateText = String.format("Tag der Inventur %s", inventory.getDate());
        String infoText = "Positive Werte beim Schwund zeigen an was mehr ist im Vergleich zu vorher. Negative Werte zeigen an was weniger ist im Vergleich zu vorher.";
        String shrinkageText = String.format("Gesamtschwund: %s €", totalShrinkage);

        // Paragraphs
        Paragraph header = new Paragraph("Inventur", titleFont);
        Paragraph date = new Paragraph(dateText, normal);
        Paragraph info = new Paragraph(infoText, disclaimer);
        Paragraph shrinkage = new Paragraph(shrinkageText, normal);

        // Tables
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        //Header
        table.addCell("Getränk");
        table.addCell("Anfangs-bestand");
        table.addCell("Gezählter Bestand");
        table.addCell("Differenz");
        table.addCell("Stückpreis EK");
        table.addCell("Gesamtwert in €");
        table.addCell("Schwund-wert in €");

        // Information
        List<InventoryEntry> entries = loadSortedEntries(inventoryId);
        for (InventoryEntry entry : entries) {
            Drink drink = drinkService.getAllDrinks().stream().filter(d -> d.getId() == entry.getDrinkId()).findAny().orElseThrow();


            table.addCell(String.valueOf(drink.getName()));
            table.addCell(String.valueOf(entry.getInitialQuantity()));
            table.addCell(String.valueOf(entry.getQuantity()));
            table.addCell(String.valueOf(entry.getShrinkage()));
            table.addCell(String.valueOf(entry.getUnitValue()));
            table.addCell(String.valueOf(entry.getTotalValue()));
            table.addCell(String.valueOf(entry.getShrinkageValue()));
        }


        // Spacing
        header.setSpacingAfter(20f);
        date.setSpacingAfter(10f);
        shrinkage.setSpacingAfter(15f);
        table.setSpacingAfter(30f);

        //Document
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(header);
        document.add(date);
        document.add(shrinkage);
        document.add(table);
        document.add(info);

        document.close();

        return out.toByteArray();
    }
}
