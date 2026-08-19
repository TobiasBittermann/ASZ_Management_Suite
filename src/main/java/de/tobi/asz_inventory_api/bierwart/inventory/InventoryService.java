package de.tobi.asz_inventory_api.bierwart.inventory;

import de.tobi.asz_inventory_api.bierwart.drink.Drink;
import de.tobi.asz_inventory_api.bierwart.drink.DrinkService;
import de.tobi.asz_inventory_api.bierwart.inventoryEntry.InventoryEntry;
import de.tobi.asz_inventory_api.bierwart.inventoryEntry.InventoryEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class InventoryService {
    private final InventoryCsvRepository repository;
    private final InventoryEntryService entryService;
    private final DrinkService drinkService;
    private final String filePath;
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    public InventoryService(InventoryCsvRepository repository,
                            InventoryEntryService entryService,
                            DrinkService drinkService,
                            @Value("${app.inventories.csv-path}") String filePath) {
        this.repository = repository;
        this.entryService = entryService;
        this.drinkService = drinkService;
        this.filePath = filePath;
    }

    public List<Inventory> getAllInventories() throws IOException {
        List<Inventory> inventories = repository.getAllInventories(filePath);
        log.debug("InventoryService loaded {} inventories", inventories.size());

        return inventories;
    }

    public void addInventory(Inventory inventory) throws IOException {
        List<Inventory> inventories = repository.getAllInventories(filePath);
        List<InventoryEntry> entries = entryService.getAllInventoryEntries();
        List<Drink> drinks = drinkService.getAllDrinks();

        long nextId = inventories.stream()
                .mapToLong(Inventory::getId)
                .max()
                .orElse(0) + 1;

        inventory.setId(nextId);

        repository.addInventory(inventories, inventory);
        repository.saveInventory(filePath, inventories);

        log.info("InventoryService added inventory with id {}", inventory.getId());

        long nextEntryId = entries.stream()
                .mapToLong(InventoryEntry::getId)
                .max()
                .orElse(0) + 1;

        for (Drink drink : drinks) {
            InventoryEntry entry = new InventoryEntry();

            entry.setId(nextEntryId++);
            entry.setInventoryId(inventory.getId());
            entry.setDrinkId(drink.getId());
            entry.setInitialQuantity(drink.getAmount());

            entry.setUnitValue(drink.getPurchasePrice());
            entry.setTotalValue(drink.getTotalValue());

            entryService.addInventoryEntry(entry);
        }
    }

    public void updateInventory(long id, Inventory inventory) throws IOException {
        List<Inventory> inventories = repository.getAllInventories(filePath);
        List<InventoryEntry> entries = entryService.getAllInventoryEntries();

        inventory.setId(id);

        repository.updateInventory(inventories, inventory);
        repository.saveInventory(filePath, inventories);

        log.info("InventoryService updated inventory with id {}", inventory.getId());
    }

    public void deleteInventory(long id) throws IOException {
        List<Inventory> inventories = repository.getAllInventories(filePath);

        repository.deleteInventory(inventories, id);
        repository.saveInventory(filePath, inventories);
    }

    public void finishInventoryIfComplete(long inventoryId) throws IOException{
        List<Inventory> inventories = repository.getAllInventories(filePath);
        Inventory inventory = inventories.stream().filter(i -> i.getId() == inventoryId).findAny().orElseThrow();

        if(inventory.isFinished()){
            return;
        }

        List<InventoryEntry> entries = entryService.getAllInventoryEntries();
        List<InventoryEntry> currentEntries = entries.stream().filter(e -> e.getInventoryId() == inventoryId).toList();
        boolean allCounted = currentEntries.stream().allMatch(e -> e.getQuantity() != null);

        if(allCounted){
            inventory.setFinished(true);
            repository.updateInventory(inventories, inventory);
            repository.saveInventory(filePath, inventories);

            List<Drink> drinks = drinkService.getAllDrinks();
            for(InventoryEntry currentEntry : currentEntries){
                if(currentEntry.getQuantity() != currentEntry.getInitialQuantity()){
                    Drink drink = drinks.stream().filter(d -> d.getId() == currentEntry.getDrinkId()).findAny().orElseThrow();
                    drink.setAmount(currentEntry.getQuantity());
                    drinkService.updateDrink(drink.getId(), drink);
                }
            }
        }
    }
}
