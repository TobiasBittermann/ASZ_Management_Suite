package de.tobi.asz_inventory_api.bierwart.inventoryEntry;

import de.tobi.asz_inventory_api.bierwart.inventory.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryEntryController {
    private final InventoryEntryService inventoryEntryService;
    private final InventoryService inventoryService;

    public InventoryEntryController(InventoryEntryService inventoryEntryService, InventoryService inventoryService) {
        this.inventoryEntryService = inventoryEntryService;
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventoryentries")
    public List<InventoryEntry> getAllInventoryEntries()  {
        return inventoryEntryService.getAllInventoryEntries();
    }

    @PostMapping("/inventoryentries")
    public void addDrink(@RequestBody InventoryEntry entry) {
        inventoryEntryService.addInventoryEntry(entry);
    }

    @PutMapping("/inventoryentries/{id}")
    public void updateInventoryEntry(@PathVariable long id, @RequestBody InventoryEntry entry)  {
        inventoryEntryService.updateInventoryEntry(id, entry);
        inventoryService.finishInventoryIfComplete(entry.getInventoryId());
    }

    @DeleteMapping("/inventoryentries/{id}")
    public void deleteInventoryEntry(@PathVariable long id) {
        inventoryEntryService.deleteInventoryEntry(id);
    }
}
