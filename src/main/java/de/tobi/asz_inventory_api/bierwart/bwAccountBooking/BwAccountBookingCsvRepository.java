package de.tobi.asz_inventory_api.bierwart.bwAccountBooking;

import de.tobi.asz_inventory_api.enums.AccountType;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BwAccountBookingCsvRepository {

    private String getBwAccountBookingsHeader() {
        Field[] fields = BwAccountBooking.class.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            header.append(fields[i].getName());

            if (i < fields.length - 1) {
                header.append(",");
            }
        }
        return header.toString();
    }

    public List<BwAccountBooking> getAllBwAccountBookings(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank.");
        }

        List<BwAccountBooking> bookings = new ArrayList<>();
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }

        if (Files.size(path) == 0) {
            Files.writeString(path, getBwAccountBookingsHeader() + System.lineSeparator());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                String[] values = line.split(",");

                BwAccountBooking booking = new BwAccountBooking();
                booking.setId(Long.parseLong(values[0]));
                booking.setVendorId(Long.parseLong(values[1]));
                booking.setAmount(new BigDecimal(values[2]));
                booking.setInvoiceNumber(values[3]);
                booking.setAccountType(AccountType.valueOf(values[4]));
                booking.setDate(LocalDateTime.parse(values[5]));
                booking.setNote(values[6]);

                bookings.add(booking);
            }
            return bookings;
        }
    }

    public void addBwAccountBooking(List<BwAccountBooking> bookings, BwAccountBooking booking) {
        bookings.add(booking);
    }

    public void updateBwAccountBooking(List<BwAccountBooking> bookings, BwAccountBooking updatedBooking) {
        for (BwAccountBooking booking : bookings) {
            if (booking.getId() == updatedBooking.getId()) {
                booking.updateFrom(updatedBooking);
                return;
            }
        }
    }

    public void deleteBwAccountBooking(List<BwAccountBooking> bookings, long id) {
        bookings.removeIf(booking -> booking.getId() == id);
    }

    public void saveBwAccountBooking(String filePath, List<BwAccountBooking> bookings) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV path must not be blank");
        }

        Path path = Path.of(filePath);
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }

        StringBuilder content = new StringBuilder();
        content.append(getBwAccountBookingsHeader()).append(System.lineSeparator());

        for (BwAccountBooking booking : bookings) {
            content.append(booking.getId()).append(",")
                    .append(booking.getVendorId()).append(",")
                    .append(booking.getAmount()).append(",")
                    .append(booking.getInvoiceNumber()).append(",")
                    .append(booking.getAccountType()).append(",")
                    .append(booking.getDate()).append(",")
                    .append(booking.getNote())
                    .append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }
}
