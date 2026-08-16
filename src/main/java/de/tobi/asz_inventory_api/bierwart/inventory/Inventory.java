package de.tobi.asz_inventory_api.bierwart.inventory;

import java.time.LocalDateTime;

public class Inventory {
    private long id;
    private LocalDateTime date;
    private long memberId;
    private boolean finished = false;
    private String note;

    public Inventory(){}

    public Inventory(Inventory other){
        this.id = other.id;
        this.date = other.date;
        this.memberId = other.memberId;
        this.finished = other.finished;
        this.note = other.note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void updateFrom(Inventory inventory){
        this.id = inventory.id;
        this.date = inventory.date;
        this.memberId = inventory.memberId;
        this.finished = inventory.finished;
        this.note = inventory.note;
    }
}
