package de.tobi.asz_inventory_api.bierwart.bwReports;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
public class BwReportController {
    private final BookingsReportService bookingsReportService;
    private final InventoryReportService inventoryReportService;

    public BwReportController(BookingsReportService bookingsReportService,
                              InventoryReportService inventoryReportService) {
        this.bookingsReportService = bookingsReportService;
        this.inventoryReportService = inventoryReportService;
    }

    @GetMapping("/reports/bookings")
    public ResponseEntity<byte[]> getBookingsReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) throws Exception {

        LocalDateTime from = dateFrom.atStartOfDay();
        LocalDateTime to = dateTo.atTime(23, 59, 59);

        byte[] pdf = bookingsReportService.generateBookingsReport(from, to);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/reports/inventory/{inventoryId}")
    public ResponseEntity<byte[]> getInventoryReport(@PathVariable long inventoryId) throws Exception {
        byte[] pdf = inventoryReportService.generateInventoryReport(inventoryId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
