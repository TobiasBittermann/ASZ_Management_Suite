package de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BwAccountSnapshot {
    private long id;
    private BigDecimal bankAccount;
    private BigDecimal cashRegister;
    private BigDecimal inventoryValue;
    private LocalDateTime snapshotDate;
    private String note;

    public BwAccountSnapshot(){
        this.bankAccount = BigDecimal.ZERO;
        this.cashRegister = BigDecimal.ZERO;
        this.inventoryValue = BigDecimal.ZERO;
    }

    public BwAccountSnapshot(BwAccountSnapshot other){
        this.id = other.id;
        this.bankAccount = other.bankAccount;
        this.cashRegister = other.cashRegister;
        this.inventoryValue = other.inventoryValue;
        this.snapshotDate = other.snapshotDate;
        this.note = other.note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BigDecimal bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(BigDecimal cashRegister) {
        this.cashRegister = cashRegister;
    }

    public BigDecimal getInventoryValue() {
        return inventoryValue;
    }

    public void setInventoryValue(BigDecimal inventoryValue) {
        this.inventoryValue = inventoryValue;
    }

    public LocalDateTime getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDateTime snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void updateFrom(BwAccountSnapshot snapshot){
        this.id = snapshot.id;
        this.bankAccount = snapshot.bankAccount;
        this.cashRegister = snapshot.cashRegister;
        this.inventoryValue = snapshot.inventoryValue;
        this.snapshotDate = snapshot.snapshotDate;
        this.note = snapshot.note;
    }
}
