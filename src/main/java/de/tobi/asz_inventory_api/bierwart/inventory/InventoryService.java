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
    private final InventoryRepository repository;
    private final InventoryEntryService entryService;
    private final DrinkService drinkService;
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    public InventoryService(InventoryRepository repository,
                            InventoryEntryService entryService,
                            DrinkService drinkService) {
        this.repository = repository;
        this.entryService = entryService;
        this.drinkService = drinkService;
    }

    public List<Inventory> getAllInventories() {
        List<Inventory> inventories = repository.findAll();
        log.debug("InventoryService loaded {} inventories", inventories.size());

        return inventories;
    }

    public void addInventory(Inventory inventory) {
        List<Drink> drinks = drinkService.getAllDrinks();

        repository.save(inventory);
        log.info("InventoryService added inventory with id {}", inventory.getId());

        for (Drink drink : drinks) {
            InventoryEntry entry = new InventoryEntry();

            entry.setInventoryId(inventory.getId());
            entry.setDrinkId(drink.getId());
            entry.setInitialQuantity(drink.getAmount());

            entry.setUnitValue(drink.getPurchasePrice());
            entry.setTotalValue(drink.getTotalValue());

            entryService.addInventoryEntry(entry);
        }
    }

    public void updateInventory(long id, Inventory inventory) {
        inventory.setId(id);
        repository.save(inventory);

        log.info("InventoryService updated inventory with id {}", inventory.getId());
    }

    public void deleteInventory(long id) {
        repository.deleteById(id);
        log.info("InventoryService deleted inventory with id {}", id);

    }

    public void finishInventoryIfComplete(long inventoryId) throws IOException {
        List<Inventory> inventories = repository.findAll();
        Inventory inventory = inventories.stream().filter(i -> i.getId() == inventoryId).findAny().orElseThrow();

        if (inventory.isFinished()) {
            return;
        }

        List<InventoryEntry> entries = entryService.getAllInventoryEntries();
        List<InventoryEntry> currentEntries = entries.stream().filter(e -> e.getInventoryId() == inventoryId).toList();
        boolean allCounted = currentEntries.stream().allMatch(e -> e.getQuantity() != null);

        if (allCounted) {
            inventory.setFinished(true);
            repository.save(inventory);

            List<Drink> drinks = drinkService.getAllDrinks();
            for (InventoryEntry currentEntry : currentEntries) {
                if (currentEntry.getQuantity() != currentEntry.getInitialQuantity()) {
                    Drink drink = drinks.stream().filter(d -> d.getId() == currentEntry.getDrinkId()).findAny().orElseThrow();
                    drink.setAmount(currentEntry.getQuantity());
                    drinkService.updateDrink(drink.getId(), drink);
                }
            }
        }
    }
}
