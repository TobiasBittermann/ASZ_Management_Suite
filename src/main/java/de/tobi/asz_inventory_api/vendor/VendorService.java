package de.tobi.asz_inventory_api.vendor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {
    private final VendorRepository repository;
    private static final Logger log = LoggerFactory.getLogger(VendorService.class);

    public VendorService(VendorRepository repository) {
        this.repository = repository;
    }

    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = repository.findAll();
        log.debug("VendorService loaded {} vendors", vendors.size());

        return vendors;
    }

    public void addVendor(Vendor vendor) {
        repository.save(vendor);
        log.info("VendorService added vendor with id {}", vendor.getId());
    }

    public void updateVendor(long id, Vendor vendor) {
        vendor.setId(id);
        repository.save(vendor);

        log.info("VendorService updated vendor with id {}", id);
    }

    public void deleteVendor(long id) {
        Vendor vendor = repository.findById(id).orElseThrow();
        repository.deleteById(id);

        log.info("VendorService deleted vendor with id {}", vendor.getId());
    }
}
