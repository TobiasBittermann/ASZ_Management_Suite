package de.tobi.asz_inventory_api.bierwart.asset;

public class Asset {
    private long id;
    private String name;
    private int amount;

    public Asset() {}

    public Asset(Asset other){
        this.id = other.id;
        this.name = other.name;
        this.amount = other.amount;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void updateFrom(Asset item){
        this.id = item.id;
        this.name = item.name;
        this.amount = item.amount;
    }
}
