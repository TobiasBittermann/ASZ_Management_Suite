package de.tobi.asz_inventory_api.bierwart.inventoryEntry;

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
    private final String filePath;
    private static final Logger log = LoggerFactory.getLogger(InventoryEntryService.class);

    public InventoryEntryService(InventoryEntryCsvRepository repository,
                                 @Value("${app.inventoryentries.csv-path}") String filePath) {
        this.repository = repository;
        this.filePath = filePath;
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
            entry.setShrinkage(entry.getQuantity() - entry.getInitialQuantity());
            entry.setShrinkageValue(entry.getUnitValue().multiply(BigDecimal.valueOf(entry.getShrinkage())));
        } else {
            entry.setShrinkage(null);
            entry.setShrinkageValue(null);
        }

        repository.updateInventoryEntry(entries, entry);
        repository.saveInventoryItem(filePath, entries);

        log.info("InventoryEntryService updated entry with id {}", entry.getId());
    }

    public void deleteInventoryEntry(long id) throws IOException {
        List<InventoryEntry> entries = repository.getAllInventoryEntries(filePath);

        repository.deleteInventoryEntry(entries, id);
        repository.saveInventoryItem(filePath, entries);
    }
}


