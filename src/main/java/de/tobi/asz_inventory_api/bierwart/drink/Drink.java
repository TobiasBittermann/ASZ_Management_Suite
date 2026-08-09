package de.tobi.asz_inventory_api.bierwart.drink;

import java.math.BigDecimal;

public class Drink {
    private long id;
    private String name;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private double factor;
    private int amount;
    private BigDecimal totalValue;

    public Drink(){}

    public Drink(Drink other){
        this.id = other.id;
        this.name = other.name;
        this.purchasePrice = other.purchasePrice;
        this.sellingPrice = other.sellingPrice;
        this.factor = other.factor;
        this.amount = other.amount;
        this.totalValue = other.totalValue;
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

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public void updateFrom(Drink drink){
        this.id = drink.id;
        this.name = drink.name;
        this.purchasePrice = drink.purchasePrice;
        this.sellingPrice = drink.sellingPrice;
        this.factor = drink.factor;
        this.amount = drink.amount;
        this.totalValue = drink.totalValue;
    }
}
