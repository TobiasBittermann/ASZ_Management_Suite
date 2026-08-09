package de.tobi.asz_inventory_api.inventoryEntry;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InventoryEntryCsvRepository {

    private String getInventoryEntryHeader() {
        Field[] fields = InventoryEntry.class.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            header.append(fields[i].getName());

            if (i < fields.length - 1) {
                header.append(",");
            }
        }
        return header.toString();
    }

    public List<InventoryEntry> getAllInventoryEntries(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
        }

        List<InventoryEntry> entries = new ArrayList<>();
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }

        if (Files.size(path) == 0) {
            Files.writeString(path, getInventoryEntryHeader() + System.lineSeparator());
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

                InventoryEntry entry = new InventoryEntry();
                entry.setId(Long.parseLong(values[0]));
                entry.setDrinkId(Long.parseLong(values[1]));
                entry.setInitialQuantity(Integer.parseInt(values[2]));
                entry.setQuantity(Integer.parseInt(values[3]));
                entry.setShrinkage(Integer.parseInt(values[4]));
                entry.setUnitValue(new BigDecimal(values[5]));
                entry.setTotalValue(new BigDecimal(values[6]));
                entry.setShrinkageValue(new BigDecimal(values[7]));

                entries.add(entry);
            }
        }
        return entries;
    }

    public void addInventoryEntry(List<InventoryEntry> entries, InventoryEntry entry) {
        entries.add(entry);
    }

    public void updateInventoryEntry(List<InventoryEntry> entries, InventoryEntry updatedEntry) {
        for (InventoryEntry entry : entries) {
            if (entry.getId() == updatedEntry.getId()) {
                entry.updateFrom(updatedEntry);
                return;
            }
        }
    }

    public void deleteInventoryEntry(List<InventoryEntry> entries, long id) {
        entries.removeIf(entry -> entry.getId() == id);
    }

    public void saveInventoryItem(String filePath, List<InventoryEntry> entries) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
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
        content.append(getInventoryEntryHeader()).append(System.lineSeparator());

        for (InventoryEntry entry : entries) {
            content.append(entry.getId()).append(",")
                    .append(entry.getDrinkId()).append(",")
                    .append(entry.getInitialQuantity()).append(",")
                    .append(entry.getQuantity()).append(",")
                    .append(entry.getShrinkage()).append(",")
                    .append(entry.getUnitValue()).append(",")
                    .append(entry.getTotalValue()).append(",")
                    .append(entry.getShrinkageValue())
                    .append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }
}

