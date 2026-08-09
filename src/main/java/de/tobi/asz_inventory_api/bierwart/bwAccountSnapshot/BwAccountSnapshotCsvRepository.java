package de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot;

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
public class BwAccountSnapshotCsvRepository {

    private String getBwAccountSnapshotsHeader() {
        Field[] fields = BwAccountSnapshot.class.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            header.append(fields[i].getName());

            if (i < fields.length - 1) {
                header.append(",");
            }
        }
        return header.toString();
    }

    public List<BwAccountSnapshot> getAllBwAccountSnapshots(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank.");
        }

        List<BwAccountSnapshot> snapshots = new ArrayList<>();
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }

        if (Files.size(path) == 0) {
            Files.writeString(path, getBwAccountSnapshotsHeader() + System.lineSeparator());
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

                BwAccountSnapshot snapshot = new BwAccountSnapshot();
                snapshot.setId(Long.parseLong(values[0]));
                snapshot.setBankAccount(new BigDecimal(values[1]));
                snapshot.setCashRegister(new BigDecimal(values[2]));
                snapshot.setInventoryValue(new BigDecimal(values[3]));
                snapshot.setSnapshotDate(LocalDateTime.parse(values[4]));
                snapshot.setNote(values[5]);

                snapshots.add(snapshot);
            }
            return snapshots;
        }
    }

    public void addBwAccountSnapshot(List<BwAccountSnapshot> snapshots, BwAccountSnapshot snapshot){
        snapshots.add(snapshot);
    }

    public void updateBwAccountSnapshot(List<BwAccountSnapshot> snapshots, BwAccountSnapshot updatedSnapshot){
        for (BwAccountSnapshot snapshot : snapshots){
            if(snapshot.getId() == updatedSnapshot.getId()){
                snapshot.updateFrom(updatedSnapshot);
                return;
            }
        }
    }

    public void deleteBwSnapshot(List<BwAccountSnapshot> snapshots, long id){
        snapshots.removeIf( snapshot -> snapshot.getId() == id);
    }

    public void saveBwAccountSnapshot(String filePath, List<BwAccountSnapshot> snapshots) throws IOException{
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
        content.append(getBwAccountSnapshotsHeader()).append(System.lineSeparator());

        for(BwAccountSnapshot snapshot : snapshots){
            content.append(snapshot.getId()).append(",")
                    .append(snapshot.getBankAccount()).append(",")
                    .append(snapshot.getCashRegister()).append(",")
                    .append(snapshot.getInventoryValue()).append(",")
                    .append(snapshot.getSnapshotDate()).append(",")
                    .append(snapshot.getNote())
                    .append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }

}
