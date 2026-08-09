package de.tobi.asz_inventory_api.bierwart.asset;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssetCsvRepository {

    private String getAssetHeader() {
        Field[] fields = Asset.class.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            header.append(fields[i].getName());

            if (i < fields.length - 1) {
                header.append(",");
            }
        }
        return header.toString();
    }

    public List<Asset> getAllAssets(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
        }

        List<Asset> assets = new ArrayList<>();
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }

        if (Files.size(path) == 0) {
            Files.writeString(path, getAssetHeader() + System.lineSeparator());
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

                Asset item = new Asset();
                item.setId(Long.parseLong(values[0]));
                item.setName(values[1]);
                item.setAmount(Integer.parseInt(values[2]));

                assets.add(item);
            }
        }
        return assets;
    }

    public void addAssets(List<Asset> assets, Asset asset) {
        assets.add(asset);
    }

    public void updateAsset(List<Asset> assets, Asset updatedAsset) {
        for (Asset item : assets) {
            if (item.getId() == updatedAsset.getId()) {
                item.updateFrom(updatedAsset);
                return;
            }
        }
    }

    public void deleteAsset(List<Asset> assets, long id) {
        assets.removeIf(item -> item.getId() == id);
    }

    public void saveAssets(String filePath, List<Asset> assets) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
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
        content.append(getAssetHeader()).append(System.lineSeparator());

        for (Asset item : assets) {
            content.append(item.getId()).append(",")
                    .append(item.getName()).append(",")
                    .append(item.getAmount())
                    .append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }
}
