package de.tobi.asz_inventory_api.vendor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String contactPerson;
    private String address;
    private String iban;

    public Vendor() {
    }

    public Vendor(Vendor other) {
        this.id = other.id;
        this.name = other.name;
        this.contactPerson = other.contactPerson;
        this.address = other.address;
        this.iban = other.iban;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void updateFrom(Vendor vendor) {
        this.id = vendor.id;
        this.name = vendor.name;
        this.contactPerson = vendor.contactPerson;
        this.address = vendor.address;
        this.iban = vendor.iban;
    }
}
