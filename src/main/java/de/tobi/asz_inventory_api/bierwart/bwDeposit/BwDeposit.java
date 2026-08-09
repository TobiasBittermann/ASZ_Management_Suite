package de.tobi.asz_inventory_api.bierwart.bwDeposit;

import de.tobi.asz_inventory_api.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BwDeposit {
    private long id;
    private long memberId;
    private BigDecimal deposit;
    private AccountType accountType;
    private LocalDateTime depositDate;
    private String description;

    public BwDeposit() {
    }

    public BwDeposit(BwDeposit other){
        this.id = other.id;
        this.memberId = other.memberId;
        this.deposit = other.deposit;
        this.accountType = other.accountType;
        this.depositDate = other.depositDate;
        this.description = other.description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public LocalDateTime getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(LocalDateTime depositDate) {
        this.depositDate = depositDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void updateFrom(BwDeposit bwDeposit){
        this.id = bwDeposit.id;
        this.memberId = bwDeposit.memberId;
        this.deposit = bwDeposit.deposit;
        this.accountType = bwDeposit.accountType;
        this.depositDate = bwDeposit.depositDate;
        this.description = bwDeposit.description;
    }
}
