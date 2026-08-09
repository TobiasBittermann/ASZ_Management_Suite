package de.tobi.asz_inventory_api.bierwart.bwAccountBooking;

import de.tobi.asz_inventory_api.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BwAccountBooking {
    private long id;
    private long vendorId;
    private BigDecimal amount;
    private String invoiceNumber;
    private AccountType accountType;
    private LocalDateTime date;
    private String note;

    public BwAccountBooking() {
    }

    public BwAccountBooking(BwAccountBooking other) {
        this.id = other.id;
        this.vendorId = other.vendorId;
        this.amount = other.amount;
        this.invoiceNumber = other.invoiceNumber;
        this.accountType = other.accountType;
        this.date = other.date;
        this.note = other.note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void updateFrom(BwAccountBooking booking) {
        this.id = booking.id;
        this.vendorId = booking.vendorId;
        this.amount = booking.amount;
        this.invoiceNumber = booking.invoiceNumber;
        this.accountType = booking.accountType;
        this.date = booking.date;
        this.note = booking.note;
    }
}
