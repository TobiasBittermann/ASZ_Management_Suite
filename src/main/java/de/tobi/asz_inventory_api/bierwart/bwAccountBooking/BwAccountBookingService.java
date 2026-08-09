package de.tobi.asz_inventory_api.bierwart.bwAccountBooking;

import de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot.BwAccountSnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BwAccountBookingService {
    private final BwAccountBookingCsvRepository repository;
    private final BwAccountSnapshotService snapshotService;
    private final String filePath;
    private static final Logger log = LoggerFactory.getLogger(BwAccountBookingService.class);

    public BwAccountBookingService(BwAccountBookingCsvRepository repository,
                                   BwAccountSnapshotService snapshotService,
                                   @Value("${app.bwaccountbookings.csv-path}") String filePath) {
        this.repository = repository;
        this.snapshotService = snapshotService;
        this.filePath = filePath;
    }

    public List<BwAccountBooking> getAllBwAccountBookings() throws IOException {
        List<BwAccountBooking> bookings = repository.getAllBwAccountBookings(filePath);
        log.debug("BwAccountBookingService loaded {} bookings", bookings.size());

        return bookings;
    }

    public void addBwAccountBooking(BwAccountBooking booking) throws IOException {
        List<BwAccountBooking> bookings = repository.getAllBwAccountBookings(filePath);

        long nextId = bookings.stream()
                .mapToLong(BwAccountBooking::getId)
                .max()
                .orElse(0) + 1;

        booking.setId(nextId);

        repository.addBwAccountBooking(bookings, booking);
        repository.saveBwAccountBooking(filePath, bookings);

        log.info("BwAccountBookingService added booking with id {}", booking.getId());

        String note = String.format("Automatische Buchung: %s vom %s %s €", booking.getInvoiceNumber(), booking.getDate(), booking.getAmount());
        snapshotService.addTransactionSnapshot(booking.getAmount(), booking.getAccountType(), note);
    }

    public void updateBwAccountBooking(long id, BwAccountBooking booking) throws IOException {
        List<BwAccountBooking> bookings = repository.getAllBwAccountBookings(filePath);

        BwAccountBooking oldBooking = bookings.stream().filter(b -> b.getId() == id).findAny().orElseThrow();
        BigDecimal oldValue = oldBooking.getAmount();

        booking.setId(id);

        repository.updateBwAccountBooking(bookings, booking);
        repository.saveBwAccountBooking(filePath, bookings);

        log.info("BwAccountBookingService updated booking with id {}", id);

        BigDecimal valueIncrease = booking.getAmount().subtract(oldValue);
        String note = String.format("Automatische Korrekturbuchung: %s vom %s %s €", booking.getInvoiceNumber(), booking.getDate(), valueIncrease);
        snapshotService.addTransactionSnapshot(valueIncrease, booking.getAccountType(), note);
    }

    public void deleteBwAccountBooking(long id) throws IOException {
        List<BwAccountBooking> bookings = repository.getAllBwAccountBookings(filePath);

        BwAccountBooking booking = bookings.stream().filter(b -> b.getId() == id).findAny().orElseThrow();

        repository.deleteBwAccountBooking(bookings, id);
        repository.saveBwAccountBooking(filePath, bookings);

        log.info("BwAccountBookingService deleted booking with id {}", booking.getId());

        String note = String.format("Automatische Rückbuchung: %s vom %s %s €", booking.getInvoiceNumber(), booking.getDate(), booking.getAmount());
        snapshotService.addTransactionSnapshot(booking.getAmount().negate(), booking.getAccountType(), note);
    }
}

