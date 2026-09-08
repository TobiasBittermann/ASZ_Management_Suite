package de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BwAccountSnapshotRepository extends JpaRepository<BwAccountSnapshot, Long> {
    Optional<BwAccountSnapshot> findTopByOrderByIdDesc();
}
