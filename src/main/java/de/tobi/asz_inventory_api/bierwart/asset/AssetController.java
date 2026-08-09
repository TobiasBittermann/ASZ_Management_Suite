package de.tobi.asz_inventory_api.bierwart.asset;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService inventoryService) {
        this.assetService = inventoryService;
    }

    @GetMapping("/assets")
    public List<Asset> getAllInventoryItems() throws IOException {
        return assetService.getAllAssets();
    }

    @PostMapping("/assets")
    public void addInventoryItem(@RequestBody Asset asset) throws IOException {
        assetService.addAsset(asset);
    }

    @PutMapping("/assets/{id}")
    public void updateInventoryItem(@PathVariable long id, @RequestBody Asset asset) throws IOException {
        assetService.updateAsset(id, asset);
    }

    @DeleteMapping("/assets/{id}")
    public void deleteInventory(@PathVariable long id) throws IOException {
        assetService.deleteAsset(id);
    }
}
