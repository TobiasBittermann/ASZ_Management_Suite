package de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BwAccountSnapshotController {

    private final BwAccountSnapshotService snapshotService;

    public BwAccountSnapshotController(BwAccountSnapshotService snapshotService){
        this.snapshotService = snapshotService;
    }

    @GetMapping("/bwsnapshots")
    public List<BwAccountSnapshot> getAllBwAccountSnapshots() {
        return snapshotService.getAllBwAccountSnapshots();
    }

    @PostMapping("/bwsnapshots")
    public void addBwAccountSnapshot(@RequestBody BwAccountSnapshot snapshot) {
        snapshotService.addBwAccountSnapshot(snapshot);
    }

    @PutMapping("/bwsnapshots/{id}")
    public void updateBwAccountSnapshot(@PathVariable long id, @RequestBody BwAccountSnapshot snapshot) {
        snapshotService.updateBwAccountSnapshot(id, snapshot);
    }

    @DeleteMapping("/bwsnapshots/{id}")
    public void deleteBwAccountSnapshot(@PathVariable long id) {
        snapshotService.deleteBwAccountSnapshot(id);
    }
}
