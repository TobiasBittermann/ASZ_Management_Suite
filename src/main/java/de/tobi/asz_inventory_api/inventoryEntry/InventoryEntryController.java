package de.tobi.asz_inventory_api.inventoryEntry;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class InventoryEntryController {
    private final InventoryEntryService inventoryEntryService;

    public InventoryEntryController(InventoryEntryService inventoryEntryService) throws IOException {
        this.inventoryEntryService = inventoryEntryService;
    }

    @GetMapping("/inventoryentries")
    public List<InventoryEntry> getAllInventoryEntries() throws IOException {
        return inventoryEntryService.getAllInventoryEntries();
    }

    @PostMapping("/inventoryentries")
    public void addDrink(@RequestBody InventoryEntry entry) throws IOException {
        inventoryEntryService.addInventoryEntry(entry);
    }

    @PutMapping("/inventoryentries/{id}")
    public void updateInventoryEntry(@PathVariable long id, @RequestBody InventoryEntry entry) throws IOException {
        inventoryEntryService.updateInventoryEntry(id, entry);
    }

    @DeleteMapping("/inventoryentries/{id}")
    public void deleteInventoryEntry(@PathVariable long id) throws IOException {
        inventoryEntryService.deleteInventoryEntry(id);
    }
}
