package de.tobi.asz_inventory_api.bierwart.bwReports;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import de.tobi.asz_inventory_api.bierwart.bwBooking.BwBooking;
import de.tobi.asz_inventory_api.bierwart.bwBooking.BwBookingService;
import de.tobi.asz_inventory_api.bierwart.bwDeposit.BwDeposit;
import de.tobi.asz_inventory_api.bierwart.bwDeposit.BwDepositService;
import de.tobi.asz_inventory_api.bierwart.drink.Drink;
import de.tobi.asz_inventory_api.bierwart.drink.DrinkService;
import de.tobi.asz_inventory_api.member.Member;
import de.tobi.asz_inventory_api.member.MemberService;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingsReportService {
    private final BwBookingService bookingService;
    private final MemberService memberService;
    private final BwDepositService depositService;
    private final DrinkService drinkService;

    public BookingsReportService(BwBookingService bookingService,
                                 MemberService memberService,
                                 BwDepositService depositService,
                                 DrinkService drinkService) {
        this.bookingService = bookingService;
        this.memberService = memberService;
        this.depositService = depositService;
        this.drinkService = drinkService;
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
    Font greenFont = new Font(Font.HELVETICA, size, Font.NORMAL, green);
    Font redFont = new Font(Font.HELVETICA, size, Font.NORMAL, red);

    public List<Member> loadSortedMembers() throws IOException {
        List<Member> members = memberService.getAllMembers().stream()
                .sorted(Comparator
                        .comparing(Member::getLastName)
                        .thenComparing(Member::getFirstName))
                .toList();

        return members;
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


    public byte[] generateBookingsReport(LocalDateTime dateFrom, LocalDateTime dateTo) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Paragraphs
        Paragraph header = new Paragraph("Bierliste", titleFont);
        Paragraph timespan = new Paragraph(String.format("Zeitraum: %s - %s", dateFrom.format(formatter), dateTo.format(formatter)), blackFont);

        // Spacing
        timespan.setSpacingAfter(15f);

        // Tables
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        List<Member> members = loadSortedMembers();
        List<BwBooking> bookings = bookingService.getAllBwBookings();
        List<BwDeposit> deposits = depositService.getAllBwDeposits();
        List<Drink> drinks = drinkService.getAllDrinks();

        for (Member member : members) {
            List<BwBooking> memberBookings = bookings.stream().filter(e -> e.getMemberId() == member.getId()).toList();
            List<BwDeposit> memberDeposits = deposits.stream().filter(d -> d.getMemberId() == member.getId()).toList();
            Map<Long, Integer> consumedDrinks = new HashMap<>();

            PdfPCell nameCell = formatCell(String.format("%s, %s", member.getLastName(), member.getFirstName()), headerWhiteFont, darkGray, false);
            nameCell.setColspan(3);
            table.addCell(nameCell);
            BigDecimal depositSum = BigDecimal.ZERO;
            BigDecimal expenditureSum = BigDecimal.ZERO;

            for (BwDeposit deposit : memberDeposits) {
                depositSum = depositSum.add(deposit.getDeposit());
            }
            for (BwBooking booking : memberBookings) {
                expenditureSum = expenditureSum.add(booking.getBookingCost());
            }

            table.addCell(formatCell("Kontostand", blackFont, lightGray, false));
            table.addCell(formatCell("Einzahlungen", blackFont, lightGray, false));
            table.addCell(formatCell("Ausgaben", blackFont, lightGray, false));
            if (member.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                table.addCell(formatCell(member.getBalance().toString(), greenFont, white, true));
            } else {
                table.addCell(formatCell(member.getBalance().toString(), redFont, white, true));
            }
            table.addCell(formatCell(depositSum.toString(), true));
            table.addCell(formatCell(expenditureSum.toString(), true));


            for (BwBooking booking : memberBookings) {
                if (!booking.getBookingDate().isBefore(dateFrom) && !booking.getBookingDate().isAfter(dateTo)) {
                    consumedDrinks.merge(booking.getDrinkId(), booking.getAmountDrink(), Integer::sum);
                }
            }

            table.addCell(formatCell("Getränke", blackFont, lightGray, false));
            table.addCell(formatCell("Anzahl", blackFont, lightGray, false));
            table.addCell(formatCell("Kosten Gesamt", blackFont, lightGray, false));

            for (Long id : consumedDrinks.keySet()) {

                Drink drink = drinks.stream().filter(d -> d.getId() == id).findAny().orElseThrow();
                String name = drink.getName();
                String amount = consumedDrinks.get(id).toString();
                String cost = drink.getSellingPrice().multiply(BigDecimal.valueOf(consumedDrinks.get(id))).setScale(2, RoundingMode.HALF_UP).toString();

                table.addCell(formatCell(name, false));
                table.addCell(formatCell(amount, true));
                table.addCell(formatCell(cost, true));
            }


            PdfPCell endCell = new PdfPCell(new Phrase(""));
            endCell.setColspan(3);
            endCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);  // kein Rahmen um die Lücke
            endCell.setFixedHeight(7f);              // Höhe der "Lücke" in Punkten
            table.addCell(endCell);
        }

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
