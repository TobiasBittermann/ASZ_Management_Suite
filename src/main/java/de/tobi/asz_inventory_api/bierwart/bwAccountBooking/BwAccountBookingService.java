package de.tobi.asz_inventory_api.bierwart.bwAccountBooking;

import de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot.BwAccountSnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BwAccountBookingService {
    private final BwAccountBookingRepository repository;
    private final BwAccountSnapshotService snapshotService;
    private static final Logger log = LoggerFactory.getLogger(BwAccountBookingService.class);

    public BwAccountBookingService(BwAccountBookingRepository repository,
                                   BwAccountSnapshotService snapshotService) {
        this.repository = repository;
        this.snapshotService = snapshotService;
    }

    public List<BwAccountBooking> getAllBwAccountBookings() {
        List<BwAccountBooking> bookings = repository.findAll();
        log.debug("BwAccountBookingService loaded {} bookings", bookings.size());

        return bookings;
    }

    public void addBwAccountBooking(BwAccountBooking booking) {
        repository.save(booking);

        log.info("BwAccountBookingService added booking with id {}", booking.getId());

        String note = String.format("Automatische Buchung: %s vom %s %s €", booking.getInvoiceNumber(), booking.getDate(), booking.getAmount());
        snapshotService.addTransactionSnapshot(booking.getAmount(), booking.getAccountType(), note);
    }

    public void updateBwAccountBooking(long id, BwAccountBooking booking) {
        BwAccountBooking oldBooking = repository.findById(id).orElseThrow();
        BigDecimal oldValue = oldBooking.getAmount();

        booking.setId(id);

        repository.save(booking);

        log.info("BwAccountBookingService updated booking with id {}", id);

        BigDecimal valueIncrease = booking.getAmount().subtract(oldValue);
        String note = String.format("Automatische Korrekturbuchung: %s vom %s %s €", booking.getInvoiceNumber(), booking.getDate(), valueIncrease);
        snapshotService.addTransactionSnapshot(valueIncrease, booking.getAccountType(), note);
    }

    public void deleteBwAccountBooking(long id) {
        BwAccountBooking booking = repository.findById(id).orElseThrow();

        repository.deleteById(id);

        log.info("BwAccountBookingService deleted booking with id {}", booking.getId());

        String note = String.format("Automatische Rückbuchung: %s vom %s %s €", booking.getInvoiceNumber(), booking.getDate(), booking.getAmount());
        snapshotService.addTransactionSnapshot(booking.getAmount().negate(), booking.getAccountType(), note);
    }
}

