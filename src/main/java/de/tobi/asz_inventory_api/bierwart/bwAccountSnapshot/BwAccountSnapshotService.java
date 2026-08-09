package de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot;

import de.tobi.asz_inventory_api.enums.AccountType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BwAccountSnapshotService {
    private final BwAccountSnapshotCsvRepository repository;
    private final String filePath;
    private static final Logger log = LoggerFactory.getLogger(BwAccountSnapshotService.class);

    public BwAccountSnapshotService(BwAccountSnapshotCsvRepository repository, @Value("${app.bwsnapshots.csv-path}") String filePath){
        this.repository = repository;
        this.filePath = filePath;
    }

    public List<BwAccountSnapshot> getAllBwAccountSnapshots() throws IOException{
        List<BwAccountSnapshot> snapshots = repository.getAllBwAccountSnapshots(filePath);
        log.debug("BwAccountSnapshotService loaded {} snapshots", snapshots.size());

        return snapshots;
    }

    public void addBwAccountSnapshot(BwAccountSnapshot snapshot) throws IOException{
        List<BwAccountSnapshot> snapshots = repository.getAllBwAccountSnapshots(filePath);

        long nextId = snapshots.stream()
                .mapToLong(BwAccountSnapshot::getId)
                .max()
                .orElse(0) + 1;

        snapshot.setId(nextId);

        repository.addBwAccountSnapshot(snapshots, snapshot);
        repository.saveBwAccountSnapshot(filePath, snapshots);

        log.info("BwAccountSnapshotService added snapshot with id {}", snapshot.getId());
    }

    public void updateBwAccountSnapshot(long id, BwAccountSnapshot snapshot) throws IOException{
        List<BwAccountSnapshot> snapshots = repository.getAllBwAccountSnapshots(filePath);

        snapshot.setId(id);

        repository.updateBwAccountSnapshot(snapshots, snapshot);
        repository.saveBwAccountSnapshot(filePath, snapshots);

        log.info("BwAccountSnapshotService updated snapshot with id {}", id);
    }

    public void deleteBwAccountSnapshot(long id) throws IOException{
        List<BwAccountSnapshot> snapshots = repository.getAllBwAccountSnapshots(filePath);

        BwAccountSnapshot snapshot = snapshots.stream().filter(s -> s.getId() == id).findAny().orElseThrow();

        repository.deleteBwSnapshot(snapshots, id);
        repository.saveBwAccountSnapshot(filePath, snapshots);

        log.info("BwAccountSnapshotService deleted snapshot with id {}", id);
    }

    public void addTransactionSnapshot(BigDecimal amount, AccountType type, String note) throws IOException {
        List<BwAccountSnapshot> snapshots = repository.getAllBwAccountSnapshots(filePath);

        long id = snapshots.stream()
                .mapToLong(BwAccountSnapshot::getId)
                .max()
                .orElse(0);

        long nextId = id + 1;

        BwAccountSnapshot snapshot = snapshots.stream().filter(s -> s.getId() == id).findAny().orElseGet(BwAccountSnapshot::new);
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

        newSnapshot.setId(nextId);
        newSnapshot.setSnapshotDate(LocalDateTime.now().withNano(0));

        repository.addBwAccountSnapshot(snapshots, newSnapshot);
        repository.saveBwAccountSnapshot(filePath, snapshots);

        log.info("BwAccountBookingService added snapshot with id {}", newSnapshot.getId());
    }
}
