package de.tobi.asz_inventory_api.bierwart.bwReports;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<byte[]> getBookingsReport() throws Exception{
        byte[] pdf = bookingsReportService.generateBookingsReport();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/reports/inventory/{inventoryId}")
    public ResponseEntity<byte[]> getInventoryReport(@PathVariable long inventoryId) throws Exception{
        byte[] pdf = inventoryReportService.generateInventoryReport(inventoryId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
