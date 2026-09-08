package de.tobi.asz_inventory_api.bierwart.bwDeposit;

import de.tobi.asz_inventory_api.enums.AccountType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BwDepositController {
    private final BwDepositService depositService;

    public BwDepositController(BwDepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping("/bwdeposits")
    public List<BwDeposit> getAllBwDeposits() {
        return depositService.getAllBwDeposits();
    }

    @GetMapping("/account-types")
    public AccountType[] getAccountTypes() {
        return AccountType.values();
    }

    @PostMapping("/bwdeposits")
    public void addBwDeposit(@RequestBody BwDeposit deposit) {
        depositService.addBwDeposit(deposit);
    }

    @PutMapping("/bwdeposits/{id}")
    public void updateBwDeposit(@PathVariable long id, @RequestBody BwDeposit deposit) {
        depositService.updateBwDeposit(id, deposit);
    }

    @DeleteMapping("/bwdeposits/{id}")
    public void deleteBwDeposit(@PathVariable long id) {
        depositService.deleteBwDeposit(id);
    }
}
