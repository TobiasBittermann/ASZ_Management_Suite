package de.tobi.asz_inventory_api.bierwart.bwReports;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import de.tobi.asz_inventory_api.bierwart.drink.Drink;
import de.tobi.asz_inventory_api.bierwart.drink.DrinkService;
import de.tobi.asz_inventory_api.bierwart.inventory.Inventory;
import de.tobi.asz_inventory_api.bierwart.inventory.InventoryService;
import de.tobi.asz_inventory_api.bierwart.inventoryEntry.InventoryEntry;
import de.tobi.asz_inventory_api.bierwart.inventoryEntry.InventoryEntryService;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class InventoryReportService {
    private final InventoryEntryService entryService;
    private final DrinkService drinkService;
    private final InventoryService inventoryService;
    private BaseFont baseFont;
    private BaseFont baseFontBold;


    public InventoryReportService(InventoryEntryService entryService,
                                  DrinkService drinkService, InventoryService inventoryService) {
        this.entryService = entryService;
        this.drinkService = drinkService;
        this.inventoryService = inventoryService;
    }

    //Formatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    //Colors
    Color white = new Color(255, 255, 255);
    Color black = new Color(0, 0, 0);
    Color lightGray = new Color(220, 220, 220);
    Color darkGray = new Color(70, 70, 70);
    Color green = new Color(0, 150, 0);
    Color red = new Color(255, 0, 0);

    // Fonts

    int size = 9;
    Font titleFont = new Font(Font.HELVETICA, 15, Font.BOLD);
    Font headerWhiteFont = new Font(Font.HELVETICA, size, Font.BOLD, white);
    Font blackFont = new Font(Font.HELVETICA, size, Font.NORMAL, black);
    Font blackBoldFont = new Font(Font.HELVETICA, size, Font.BOLD, black);
    Font greenFont = new Font(Font.HELVETICA, size, Font.NORMAL, green);
    Font redFont = new Font(Font.HELVETICA, size, Font.NORMAL, red);
    Font disclaimer = new Font(Font.HELVETICA, 7, Font.NORMAL, black);

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

        for (InventoryEntry entry : entries) {
            total = total.add(entry.getShrinkageValue());
        }

        return total;
    }

    private PdfPCell formatCell(String text, boolean alignRight) {
        return formatCell(text, blackFont, white, alignRight);
    }

    private PdfPCell formatCell(String text, Font font, Color color, boolean alignRight) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(color);
        cell.setBorderColor(lightGray);
        cell.setBorderWidth(0.5f);
        cell.setPadding(6f);
        if (alignRight) {
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }
        return cell;
    }

    public byte[] generateInventoryReport(long inventoryId) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Inventory inventory = inventoryService.getAllInventories().stream().filter(i -> i.getId() == inventoryId).findAny().orElseThrow();
        BigDecimal totalShrinkage = getTotalShrinkage(inventoryId);

        //Texts
        String dateText = String.format("Tag der Inventur: %s", inventory.getDate().format(formatter));
        String infoText = "Positive Werte beim Schwund zeigen an was mehr ist im Vergleich zu vorher. Negative Werte zeigen an was weniger ist im Vergleich zu vorher.";
        String shrinkageText = String.format("Gesamtschwund: %s €", totalShrinkage);

        // Paragraphs
        Paragraph header = new Paragraph("Inventur", titleFont);
        Paragraph date = new Paragraph(dateText, blackFont);
        Paragraph info = new Paragraph(infoText, disclaimer);
        Paragraph shrinkage = new Paragraph(shrinkageText, blackFont);

        // Tables
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        //Header
        table.addCell(formatCell("Getränk", blackFont, lightGray, false));
        table.addCell(formatCell("Sollbestand", blackFont, lightGray, false));
        table.addCell(formatCell("Istbestand", blackFont, lightGray, false));
        table.addCell(formatCell("Differenz", blackFont, lightGray, false));
        table.addCell(formatCell("EK [€]", blackFont, lightGray, false));
        table.addCell(formatCell("Gesamtwert [€]", blackFont, lightGray, false));
        table.addCell(formatCell("Schwund [€]", blackFont, lightGray, false));

        // Information
        List<InventoryEntry> entries = loadSortedEntries(inventoryId);
        for (InventoryEntry entry : entries) {
            Drink drink = drinkService.getAllDrinks().stream().filter(d -> d.getId() == entry.getDrinkId()).findAny().orElseThrow();

            table.addCell(formatCell(String.valueOf(drink.getName()), true));
            table.addCell(formatCell(String.valueOf(entry.getInitialQuantity()), true));
            table.addCell(formatCell(String.valueOf(entry.getQuantity()), true));
            table.addCell(formatCell(String.valueOf(entry.getShrinkage()), true));
            table.addCell(formatCell(String.valueOf(entry.getUnitValue()), true));
            table.addCell(formatCell(String.valueOf(entry.getTotalValue()), true));
            if (entry.getShrinkageValue().compareTo(BigDecimal.ZERO) > 0) {
                table.addCell(formatCell(String.valueOf(entry.getShrinkageValue()), greenFont, white, true));

            } else {
                table.addCell(formatCell(String.valueOf(entry.getShrinkageValue()), redFont, white, true));

            }
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
