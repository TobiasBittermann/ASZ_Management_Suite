package de.tobi.asz_inventory_api.inventory;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InventoryCsvRepository {

    private String getInventoryHeader() {
        Field[] fields = Inventory.class.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            header.append(fields[i].getName());

            if (i < fields.length - 1) {
                header.append(";");
            }
        }
        return header.toString();
    }

    public List<Inventory> getAllInventories(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
        }

        List<Inventory> inventories = new ArrayList<>();
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }

        if (Files.size(path) == 0) {
            Files.writeString(path, getInventoryHeader() + System.lineSeparator());
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

                String[] values = line.split(";");

                Inventory inventory = new Inventory();
                inventory.setId(Long.parseLong(values[0]));
                inventory.setDate(LocalDateTime.parse(values[1]));
                inventory.setMemberId(Long.parseLong(values[2]));
                inventory.setFinished(Boolean.parseBoolean(values[3]));
                inventory.setNote(values[4]);

                inventories.add(inventory);
            }
            return inventories;
        }
    }

    public void addInventory(List<Inventory> inventories, Inventory inventory) {
        inventories.add(inventory);
    }

    public void updateInventory(List<Inventory> inventories, Inventory updatedInventory) {
        for (Inventory inventory : inventories) {
            if (inventory.getId() == updatedInventory.getId()) {
                inventory.updateFrom(updatedInventory);
                return;
            }
        }
    }

    public void deleteInventory(List<Inventory> inventories, long id) throws IOException {
        inventories.removeIf(inventory -> inventory.getId() == id);
    }

    public void saveInventory(String filePath, List<Inventory> inventories) throws IOException {
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
        content.append(getInventoryHeader()).append(System.lineSeparator());

        for (Inventory inventory : inventories) {
            content.append(inventory.getId()).append(";")
                    .append(inventory.getDate()).append(";")
                    .append(inventory.getMemberId()).append(";")
                    .append(inventory.isFinished()).append(";")
                    .append(inventory.getNote())
                    .append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }
}
