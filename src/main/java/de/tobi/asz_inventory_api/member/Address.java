package de.tobi.asz_inventory_api.member;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String country;

    public Address(){}

    public Address(Address other){
        this.street = other.street;
        this.houseNumber = other.houseNumber;
        this.postalCode = other.postalCode;
        this.city = other.city;
        this.country = other.country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void updateFrom(Address address){
        this.street = address.street;
        this.houseNumber = address.houseNumber;
        this.postalCode = address.postalCode;
        this.city = address.city;
        this.country = address.country;
    }
}
