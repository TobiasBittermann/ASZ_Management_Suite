package de.tobi.asz_inventory_api.inventory;

import de.tobi.asz_inventory_api.bierwart.drink.Drink;
import de.tobi.asz_inventory_api.bierwart.drink.DrinkCsvRepository;
import de.tobi.asz_inventory_api.inventoryEntry.InventoryEntry;
import de.tobi.asz_inventory_api.inventoryEntry.InventoryEntryCsvRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class InventoryService {
    private final InventoryCsvRepository repository;
    private final InventoryEntryCsvRepository entryRepository;
    private final DrinkCsvRepository drinkRepository;
    private final String filePath;
    private final String entryFilePath;
    private final String drinkFilePath;
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    public InventoryService(InventoryCsvRepository repository,
                            InventoryEntryCsvRepository entryRepository,
                            DrinkCsvRepository drinkRepository,
                            @Value("${app.inventories.csv-path}") String filePath,
                            @Value("${app.inventoryentries.csv-path}") String entryFilePath,
                            @Value("${app.drinks.csv-path}") String drinkFilePath) {
        this.repository = repository;
        this.entryRepository = entryRepository;
        this.drinkRepository = drinkRepository;
        this.filePath = filePath;
        this.entryFilePath = entryFilePath;
        this.drinkFilePath = drinkFilePath;
    }

    public List<Inventory> getAllInventories() throws IOException {
        List<Inventory> inventories = repository.getAllInventories(filePath);
        log.debug("InventoryService loaded {} inventories", inventories.size());

        return inventories;
    }

    public void addInventory(Inventory inventory) throws IOException {
        List<Inventory> inventories = repository.getAllInventories(filePath);
        List<InventoryEntry> entries = entryRepository.getAllInventoryEntries(entryFilePath);
        List<Drink> drinks = drinkRepository.getAllDrinks(drinkFilePath);

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

            entryRepository.addInventoryEntry(entries, entry);
        }

        entryRepository.saveInventoryItem(entryFilePath, entries);
    }

    public void updateInventory(long id, Inventory inventory) throws IOException {
        List<Inventory> inventories = repository.getAllInventories(filePath);

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
}
