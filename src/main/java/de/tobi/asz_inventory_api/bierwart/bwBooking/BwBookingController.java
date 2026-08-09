package de.tobi.asz_inventory_api.bierwart.bwBooking;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class BwBookingController {

    private final BwBookingService bookingService;

    public BwBookingController(BwBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/bwbookings")
    public List<BwBooking> getAllBwBookings() throws IOException {
        return bookingService.getAllBwBookings();
    }

    @PostMapping("/bwbookings")
    public void addBwBooking(@RequestBody BwBooking booking) throws IOException {
        bookingService.addBwBooking(booking);
    }

    @PutMapping("/bwbookings/{id}")
    public void updateBwBooking(@PathVariable long id, @RequestBody BwBooking booking) throws IOException {
        bookingService.updateBwBooking(id, booking);
    }

    @DeleteMapping("/bwbookings/{id}")
    public void deleteBwBooking(@PathVariable long id) throws IOException {
        bookingService.deleteBwBooking(id);
    }
}
