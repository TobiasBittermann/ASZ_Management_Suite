package de.tobi.asz_inventory_api.bierwart.bwAccountBooking;

import de.tobi.asz_inventory_api.enums.AccountType;
import de.tobi.asz_inventory_api.vendor.Vendor;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class BwAccountBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    private BigDecimal amount;
    private String invoiceNumber;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private LocalDateTime date;
    private String note;

    public BwAccountBooking() {
    }

    public BwAccountBooking(BwAccountBooking other) {
        this.id = other.id;
        this.vendor = other.vendor;
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

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
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
        this.vendor = booking.vendor;
        this.amount = booking.amount;
        this.invoiceNumber = booking.invoiceNumber;
        this.accountType = booking.accountType;
        this.date = booking.date;
        this.note = booking.note;
    }
}
