package de.tobi.asz_inventory_api.bierwart.bwReports;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class BookingsReportService {
    public byte [] generateBookingsReport() throws DocumentException{
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Fonts
        Font titleFont = new Font(Font.HELVETICA, 15, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 10);

        // Paragraphs
        Paragraph header = new Paragraph("Bierliste", titleFont);
        Paragraph timespan = new Paragraph("Zeitraum: ${} - ${}", normal);

        // Spacing
        timespan.setSpacingAfter(15f);

        // Tables
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        // Person
        PdfPCell wideCell = new PdfPCell(new Phrase("Tobi Bittermann"));
        wideCell.setColspan(3);
        table.addCell(wideCell);
        // General information
        table.addCell("Kontostand");
        table.addCell("Ausgaben");
        table.addCell("Einzahlungen");
        table.addCell("98,35€");
        table.addCell("4,70€");
        table.addCell("100,00€");
        // Sum up of drinks
        table.addCell("Getränke");
        table.addCell("Anzahl");
        table.addCell("Gesamtkosten");
        table.addCell("Cola");
        table.addCell("2");
        table.addCell("2,28€");

        // Document
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(header);
        document.add(timespan);
        document.add(table);

        document.close();

        return out.toByteArray();
    }
}
