package de.tobi.asz_inventory_api.vendor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("vendors")
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @PostMapping("vendors")
    public void addVendor(@RequestBody Vendor vendor) {
        vendorService.addVendor(vendor);
    }

    @PutMapping("vendors/{id}")
    public void updateVendor(@PathVariable long id, @RequestBody Vendor vendor) {
        vendorService.updateVendor(id, vendor);
    }

    @DeleteMapping("vendors/{id}")
    public void deleteVendor(@PathVariable long id) {
        vendorService.deleteVendor(id);
    }
}
