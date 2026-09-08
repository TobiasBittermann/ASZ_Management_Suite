package de.tobi.asz_inventory_api.bierwart.bwBooking;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BwBookingController {

    private final BwBookingService bookingService;

    public BwBookingController(BwBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/bwbookings")
    public List<BwBooking> getAllBwBookings() {
        return bookingService.getAllBwBookings();
    }

    @PostMapping("/bwbookings")
    public void addBwBooking(@RequestBody BwBooking booking) {
        bookingService.addBwBooking(booking);
    }

    @PutMapping("/bwbookings/{id}")
    public void updateBwBooking(@PathVariable long id, @RequestBody BwBooking booking) {
        bookingService.updateBwBooking(id, booking);
    }

    @DeleteMapping("/bwbookings/{id}")
    public void deleteBwBooking(@PathVariable long id) {
        bookingService.deleteBwBooking(id);
    }
}
