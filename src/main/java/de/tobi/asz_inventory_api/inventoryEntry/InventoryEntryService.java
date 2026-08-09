package de.tobi.asz_inventory_api.inventoryEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

public class InventoryEntryService {
    private final InventoryEntryCsvRepository repository;
    private final String filePath;
    private static final Logger log = LoggerFactory.getLogger(InventoryEntryService.class);

    public InventoryEntryService(InventoryEntryCsvRepository repository, @Value("${app.inventoryentries.csv-path}") String filePath) {
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
                .mapToLong(InventoryEntry::getDrinkId)
                .max()
                .orElse(0) + 1;

        entry.setId(nextId);

        repository.addInventoryEntry(entries, entry);
        repository.saveInventoryItem(filePath, entries);

        log.info("InventoryEntryService added entry with id {}.", entry.getId());
    }


}


