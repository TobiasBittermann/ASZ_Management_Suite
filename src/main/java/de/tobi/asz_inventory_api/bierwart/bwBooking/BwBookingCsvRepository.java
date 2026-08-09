package de.tobi.asz_inventory_api.bierwart.bwBooking;

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
public class BwBookingCsvRepository {

    private String getBwBookingsHeader() {
        Field[] fields = BwBooking.class.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            header.append(fields[i].getName());

            if (i < fields.length - 1) {
                header.append(",");
            }
        }
        return header.toString();
    }

    public List<BwBooking> getAllBwBookings(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
        }

        List<BwBooking> bookings = new ArrayList<>();
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }

        if (Files.size(path) == 0) {
            Files.writeString(path, getBwBookingsHeader() + System.lineSeparator());
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

                BwBooking booking = new BwBooking();
                booking.setId(Long.parseLong(values[0]));
                booking.setMemberId(Long.parseLong(values[1]));
                booking.setDrinkId(Long.parseLong(values[2]));
                booking.setAmountDrink(Integer.parseInt(values[3]));
                booking.setBookingDate(LocalDateTime.parse(values[4]));
                booking.setBookingCost(new BigDecimal(values[5]));

                bookings.add(booking);
            }
            return bookings;
        }
    }

    public void addBwBooking(List<BwBooking> bookings, BwBooking booking) {
        bookings.add(booking);
    }

    public void updateBwBooking(List<BwBooking> bookings, BwBooking updatedBooking) {
        for (BwBooking booking : bookings) {
            if (booking.getId() == updatedBooking.getId()) {
                booking.updateFrom(updatedBooking);
                return;
            }
        }
    }

    public void deleteBwBooking(List<BwBooking> bookings, long id) {
        bookings.removeIf(booking -> booking.getId() == id);
    }

    public void saveBwBooking(String filePath, List<BwBooking> bookings) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV path must not be blank");
        }

        Path path = Path.of(filePath);
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if(Files.notExists(path)){
            Files.createFile(path);
        }

        StringBuilder content = new StringBuilder();
        content.append(getBwBookingsHeader()).append(System.lineSeparator());

        for (BwBooking booking: bookings){
            content.append(booking.getId()).append(",")
                    .append(booking.getMemberId()).append(",")
                    .append(booking.getDrinkId()).append(",")
                    .append(booking.getAmountDrink()).append(",")
                    .append(booking.getBookingDate()).append(",")
                    .append(booking.getBookingCost())
                    .append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }
}

