package de.tobi.asz_inventory_api.bierwart.inventoryEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryEntryService {
    private final InventoryEntryRepository repository;
    private static final Logger log = LoggerFactory.getLogger(InventoryEntryService.class);

    public InventoryEntryService(InventoryEntryRepository repository) {
        this.repository = repository;
    }

    public List<InventoryEntry> getAllInventoryEntries() {
        List<InventoryEntry> entries = repository.findAll();
        log.debug("InventoryEntryService loaded {} entries", entries.size());

        return entries;
    }

    public void addInventoryEntry(InventoryEntry entry) {
        repository.save(entry);
        log.info("InventoryEntryService added entry with id {}.", entry.getId());
    }

    public void updateInventoryEntry(long id, InventoryEntry entry) {
        entry.setId(id);
        if (entry.getQuantity() != null) {
            entry.setShrinkage(entry.getQuantity() - entry.getInitialQuantity());
            entry.setShrinkageValue(entry.getUnitValue().multiply(BigDecimal.valueOf(entry.getShrinkage())));
        } else {
            entry.setShrinkage(null);
            entry.setShrinkageValue(null);
        }
        repository.save(entry);

        log.info("InventoryEntryService updated entry with id {}", entry.getId());
    }

    public void deleteInventoryEntry(long id) {
        repository.deleteById(id);
        log.info("InventoryEntryService deleted entry with id {}", id);
    }
}


