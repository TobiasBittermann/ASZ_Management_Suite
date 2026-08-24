package de.tobi.asz_inventory_api.bierwart.bwReports;

import com.lowagie.text.*;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public List<Member> loadSortedMembers() throws IOException {
        List<Member> members = memberService.getAllMembers().stream()
                .sorted(Comparator
                        .comparing(Member::getLastName)
                        .thenComparing(Member::getFirstName))
                .toList();

        return members;
    }


    public byte[] generateBookingsReport(LocalDateTime dateFrom, LocalDateTime dateTo) throws DocumentException, IOException {
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

        List<Member> members = loadSortedMembers();
        List<BwBooking> bookings = bookingService.getAllBwBookings();
        List<BwDeposit> deposits = depositService.getAllBwDeposits();
        List<Drink> drinks = drinkService.getAllDrinks();

        for (Member member : members) {
            List<BwBooking> memberBookings = bookings.stream().filter(e -> e.getMemberId() == member.getId()).toList();
            List<BwDeposit> memberDeposits = deposits.stream().filter(d -> d.getMemberId() == member.getId()).toList();
            Map<Long, Integer> consumedDrinks = new HashMap<>();

            PdfPCell wideCell = new PdfPCell(new Phrase(String.format("%s, %s", member.getLastName(), member.getFirstName())));
            wideCell.setColspan(3);
            table.addCell(wideCell);
            BigDecimal depositSum = BigDecimal.ZERO;
            BigDecimal expenditureSum = BigDecimal.ZERO;

            for (BwDeposit deposit : memberDeposits) {
                depositSum = depositSum.add(deposit.getDeposit());
            }
            for (BwBooking booking : memberBookings) {
                expenditureSum = expenditureSum.add(booking.getBookingCost());
            }

            table.addCell("Kontostand");
            table.addCell("Ausgaben");
            table.addCell("Einzahlungen");
            table.addCell(member.getBalance().toString());
            table.addCell(expenditureSum.toString());
            table.addCell(depositSum.toString());


            for (BwBooking booking : memberBookings) {
                if (!booking.getBookingDate().isBefore(dateFrom) && !booking.getBookingDate().isAfter(dateTo)) {
                    consumedDrinks.merge(booking.getDrinkId(), booking.getAmountDrink(), Integer::sum);
                }
            }

            table.addCell("Getränke");
            table.addCell("Anzahl");
            table.addCell("Kosten Gesamt");

            for (Long id : consumedDrinks.keySet()) {

                Drink drink = drinks.stream().filter(d -> d.getId() == id).findAny().orElseThrow();
                String name = drink.getName();
                String amount = consumedDrinks.get(id).toString();
                String cost = drink.getSellingPrice().multiply(BigDecimal.valueOf(consumedDrinks.get(id))).toString();

                table.addCell(name);
                table.addCell(amount);
                table.addCell(cost);
            }

            PdfPCell endCell = new PdfPCell(new Phrase(""));
            endCell.setColspan(3);
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
