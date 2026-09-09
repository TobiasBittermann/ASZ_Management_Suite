package de.tobi.asz_inventory_api.bierwart.asset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository repository;
    private static final Logger log = LoggerFactory.getLogger(AssetService.class);

    public AssetService(AssetRepository repository) {
        this.repository = repository;
    }

    public List<Asset> getAllAssets() {
        List<Asset> inventoryItems = repository.findAll();
        log.debug("InventoryService loaded {} items.", inventoryItems.size());

        return inventoryItems;
    }

    public void addAsset(Asset item) {
        repository.save(item);
        log.info("InventoryService added item {} with id {}.", item.getName(), item.getId());
    }

    public void updateAsset(long id, Asset item) {
        item.setId(id);
        repository.save(item);
        log.info("InventoryService updated item {} with id {}.", item.getName(), item.getId());
    }

    public void deleteAsset(long id) {
        Asset item = repository.findById(id).orElseThrow();
        repository.deleteById(id);
        log.info("InventoryService deleted item {} with id {}.", item.getName(), item.getId());
    }
}
