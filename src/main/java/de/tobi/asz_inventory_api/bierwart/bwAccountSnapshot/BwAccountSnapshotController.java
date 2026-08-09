package de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class BwAccountSnapshotController {

    private final BwAccountSnapshotService snapshotService;

    public BwAccountSnapshotController(BwAccountSnapshotService snapshotService){
        this.snapshotService = snapshotService;
    }

    @GetMapping("/bwsnapshots")
    public List<BwAccountSnapshot> getAllBwAccountSnapshots() throws IOException{
        return snapshotService.getAllBwAccountSnapshots();
    }

    @PostMapping("/bwsnapshots")
    public void addBwAccountSnapshot(@RequestBody BwAccountSnapshot snapshot) throws IOException{
        snapshotService.addBwAccountSnapshot(snapshot);
    }

    @PutMapping("/bwsnapshots/{id}")
    public void updateBwAccountSnapshot(@PathVariable long id, @RequestBody BwAccountSnapshot snapshot) throws IOException{
        snapshotService.updateBwAccountSnapshot(id, snapshot);
    }

    @DeleteMapping("/bwsnapshots/{id}")
    public void deleteBwAccountSnapshot(@PathVariable long id) throws IOException{
        snapshotService.deleteBwAccountSnapshot(id);
    }
}
