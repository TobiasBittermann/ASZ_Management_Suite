package de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot;

import de.tobi.asz_inventory_api.enums.AccountType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BwAccountSnapshotService {
    private final BwAccountSnapshotRepository repository;
    private static final Logger log = LoggerFactory.getLogger(BwAccountSnapshotService.class);

    public BwAccountSnapshotService(BwAccountSnapshotRepository repository) {
        this.repository = repository;
    }

    public List<BwAccountSnapshot> getAllBwAccountSnapshots() {
        List<BwAccountSnapshot> snapshots = repository.findAll();
        log.debug("BwAccountSnapshotService loaded {} snapshots", snapshots.size());

        return snapshots;
    }

    public void addBwAccountSnapshot(BwAccountSnapshot snapshot) {
        repository.save(snapshot);

        log.info("BwAccountSnapshotService added snapshot with id {}", snapshot.getId());
    }

    public void updateBwAccountSnapshot(long id, BwAccountSnapshot snapshot) {
        snapshot.setId(id);
        repository.save(snapshot);

        log.info("BwAccountSnapshotService updated snapshot with id {}", id);
    }

    public void deleteBwAccountSnapshot(long id) {
        repository.deleteById(id);

        log.info("BwAccountSnapshotService deleted snapshot with id {}", id);
    }

    public void addTransactionSnapshot(BigDecimal amount, AccountType type, String note) {
        //Old snapshot is necessary to carry over unchanged values to the new snapshot
        BwAccountSnapshot snapshot = repository.findTopByOrderByIdDesc().orElseGet(BwAccountSnapshot::new);
        BwAccountSnapshot newSnapshot = new BwAccountSnapshot(snapshot);

        switch (type) {
            case BANK_ACCOUNT:
                newSnapshot.setBankAccount(newSnapshot.getBankAccount().add(amount));
                newSnapshot.setNote(note);
                break;
            case CASH_REGISTER:
                newSnapshot.setCashRegister(newSnapshot.getCashRegister().add(amount));
                newSnapshot.setNote(note);
                break;
            case INVENTORY:
                newSnapshot.setInventoryValue(newSnapshot.getInventoryValue().add(amount));
                newSnapshot.setNote(note);
        }

        //Has to be reset to standard value so te DB can set the correct value
        newSnapshot.setId(0);
        newSnapshot.setSnapshotDate(LocalDateTime.now().withNano(0));

        repository.save(newSnapshot);

        log.info("BwAccountBookingService added snapshot with id {}", newSnapshot.getId());
    }
}
