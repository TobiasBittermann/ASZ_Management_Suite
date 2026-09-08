package de.tobi.asz_inventory_api.bierwart.inventory;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController (InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventories")
    public List<Inventory> getAllInventories()  {
        return inventoryService.getAllInventories();
    }

    @PostMapping("/inventories")
    public void addInventory(@RequestBody Inventory inventory)  {
        inventoryService.addInventory(inventory);
    }

    @PutMapping("/inventories/{id}")
    public void updateInventory(@PathVariable long id, @RequestBody Inventory inventory)  {
        inventoryService.updateInventory(id, inventory);
    }

    @DeleteMapping("/inventories/{id}")
    public void deleteInventory(@PathVariable long id)  {
        inventoryService.deleteInventory(id);
    }
}
