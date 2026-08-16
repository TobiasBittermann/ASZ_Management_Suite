package de.tobi.asz_inventory_api.bierwart.inventoryEntry;

import de.tobi.asz_inventory_api.bierwart.inventory.Inventory;
import de.tobi.asz_inventory_api.bierwart.inventory.InventoryCsvRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryEntryService {
    private final InventoryEntryCsvRepository repository;
    private final InventoryCsvRepository inventoryRepository;
    private final String filePath;
    private final String inventoryFilePath;
    private static final Logger log = LoggerFactory.getLogger(InventoryEntryService.class);

    public InventoryEntryService(InventoryEntryCsvRepository repository,
                                 InventoryCsvRepository inventoryRepository,
                                 @Value("${app.inventoryentries.csv-path}") String filePath,
                                 @Value("${app.inventories.csv-path}") String inventoryFilePath) {
        this.repository = repository;
        this.inventoryRepository = inventoryRepository;
        this.filePath = filePath;
        this.inventoryFilePath = inventoryFilePath;
    }

    public List<InventoryEntry> getAllInventoryEntries() throws IOException {
        List<InventoryEntry> entries = repository.getAllInventoryEntries(filePath);
        log.debug("InventoryEntryService loaded {} entries", entries.size());

        return entries;
    }

    public void addInventoryEntry(InventoryEntry entry) throws IOException {
        List<InventoryEntry> entries = repository.getAllInventoryEntries(filePath);

        long nextId = entries.stream()
                .mapToLong(InventoryEntry::getId)
                .max()
                .orElse(0) + 1;

        entry.setId(nextId);

        repository.addInventoryEntry(entries, entry);
        repository.saveInventoryItem(filePath, entries);

        log.info("InventoryEntryService added entry with id {}.", entry.getId());
    }

    public void updateInventoryEntry(long id, InventoryEntry entry) throws IOException {
        List<InventoryEntry> entries = repository.getAllInventoryEntries(filePath);

        entry.setId(id);
        if (entry.getQuantity() != null) {
            entry.setShrinkage(entry.getInitialQuantity() - entry.getQuantity());
            entry.setShrinkageValue(entry.getUnitValue().multiply(BigDecimal.valueOf(entry.getShrinkage())));
        } else {
            entry.setShrinkage(null);
            entry.setShrinkageValue(null);
        }

        repository.updateInventoryEntry(entries, entry);
        repository.saveInventoryItem(filePath, entries);

        log.info("InventoryEntryService updated entry with id {}", entry.getId());

        checkInventoryFinished(entries, entry);

    }

    public void deleteInventoryEntry(long id) throws IOException {
        List<InventoryEntry> entries = repository.getAllInventoryEntries(filePath);

        repository.deleteInventoryEntry(entries, id);
        repository.saveInventoryItem(filePath, entries);
    }

    public void checkInventoryFinished(List<InventoryEntry> entries, InventoryEntry entry)throws IOException{
        List<Inventory> inventories = inventoryRepository.getAllInventories(inventoryFilePath);
        Inventory inventory = inventories.stream().filter(i -> i.getId() == entry.getInventoryId()).findAny().orElseThrow();

        if(!inventory.isFinished()){
            List<InventoryEntry> currentEntries = entries.stream().filter(e -> e.getInventoryId() == entry.getInventoryId()).toList();
            boolean allCounted = currentEntries.stream().allMatch(e -> e.getQuantity() != null);

            if(allCounted){
                inventory.setFinished(true);

                inventoryRepository.updateInventory(inventories, inventory);
                inventoryRepository.saveInventory(inventoryFilePath, inventories);
            }
        }
    }

}


