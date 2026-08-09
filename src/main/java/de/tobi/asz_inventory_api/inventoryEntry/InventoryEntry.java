package de.tobi.asz_inventory_api.inventoryEntry;

import java.math.BigDecimal;

public class InventoryEntry {
    private long id;
    private long drinkId;
    private int initialQuantity;
    private int quantity;
    private int shrinkage;
    private BigDecimal unitValue;
    private BigDecimal totalValue;
    private BigDecimal shrinkageValue;

    public InventoryEntry() {}

    //TODO: Add inventory id
    public InventoryEntry(InventoryEntry other) {
        this.id = other.id;
        this.drinkId = other.drinkId;
        this.initialQuantity = other.initialQuantity;
        this.quantity = other.quantity;
        this.shrinkage = other.shrinkage;
        this.unitValue = other.unitValue;
        this.totalValue = other.totalValue;
        this.shrinkageValue = other.shrinkageValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(long drinkId) {
        this.drinkId = drinkId;
    }

    public int getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getShrinkage() {
        return shrinkage;
    }

    public void setShrinkage(int shrinkage) {
        this.shrinkage = shrinkage;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getShrinkageValue() {
        return shrinkageValue;
    }

    public void setShrinkageValue(BigDecimal shrinkageValue) {
        this.shrinkageValue = shrinkageValue;
    }

    public void updateFrom (InventoryEntry entry){
        this.id = entry.id;
        this.drinkId = entry.drinkId;
        this.initialQuantity = entry.initialQuantity;
        this.quantity = entry.quantity;
        this.shrinkage = entry.shrinkage;
        this.unitValue = entry.unitValue;
        this.totalValue = entry.totalValue;
        this.shrinkageValue = entry.shrinkageValue;
    }
}
