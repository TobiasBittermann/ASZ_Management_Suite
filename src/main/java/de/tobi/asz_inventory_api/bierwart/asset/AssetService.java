package de.tobi.asz_inventory_api.bierwart.asset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AssetService {

    private final AssetCsvRepository repository;
    private final String filePath;
    private static final Logger log = LoggerFactory.getLogger(AssetService.class);

    public AssetService(AssetCsvRepository repository, @Value("${app.assets.csv-path}") String filePath) {
        this.repository = repository;
        this.filePath = filePath;
    }

    public List<Asset> getAllAssets() throws IOException {
        List<Asset> inventoryItems = repository.getAllAssets(filePath);
        log.debug("InventoryService loaded {} items.", inventoryItems.size());

        return inventoryItems;
    }

    public void addAsset(Asset item) throws IOException {
        List<Asset> inventoryItems = repository.getAllAssets(filePath);

        long nextId = inventoryItems.stream()
                .mapToLong(Asset::getId)
                .max()
                .orElse(0) + 1;

        item.setId(nextId);

        repository.addAssets(inventoryItems, item);
        repository.saveAssets(filePath, inventoryItems);

        log.info("InventoryService added item {} with id {}.", item.getName(), item.getId());
    }

    public void updateAsset(long id, Asset item) throws IOException {
        List<Asset> inventoryItems = repository.getAllAssets(filePath);

        item.setId(id);

        repository.updateAsset(inventoryItems, item);
        repository.saveAssets(filePath, inventoryItems);

        log.info("InventoryService updated item {} with id {}.", item.getName(), item.getId());
    }

    public void deleteAsset(long id) throws IOException {
        List<Asset> inventoryItems = repository.getAllAssets(filePath);

        Asset item = inventoryItems.stream().filter(i -> i.getId() == id).findAny().orElseThrow();

        repository.deleteAsset(inventoryItems, id);
        repository.saveAssets(filePath, inventoryItems);

        log.info("InventoryService deleted item {} with id {}.", item.getName(), item.getId());
    }
}
