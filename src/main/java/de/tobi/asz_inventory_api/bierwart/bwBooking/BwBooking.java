package de.tobi.asz_inventory_api.bierwart.bwBooking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BwBooking {
    private long id;
    private long memberId;
    private long drinkId;
    private int amountDrink;
    private LocalDateTime bookingDate;
    private BigDecimal bookingCost;

    public BwBooking() {
    }

    public BwBooking(BwBooking other) {
        this.id = other.id;
        this.memberId = other.memberId;
        this.drinkId = other.drinkId;
        this.amountDrink = other.amountDrink;
        this.bookingDate = other.bookingDate;
        this.bookingCost = other.bookingCost;
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

    public long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(long drinkId) {
        this.drinkId = drinkId;
    }

    public int getAmountDrink() {
        return amountDrink;
    }

    public void setAmountDrink(int amountDrink) {
        this.amountDrink = amountDrink;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getBookingCost() {
        return bookingCost;
    }

    public void setBookingCost(BigDecimal bookingCost) {
        this.bookingCost = bookingCost;
    }

    public void updateFrom(BwBooking bwBooking) {
        this.id = bwBooking.id;
        this.memberId = bwBooking.memberId;
        this.drinkId = bwBooking.drinkId;
        this.amountDrink = bwBooking.amountDrink;
        this.bookingDate = bwBooking.bookingDate;
        this.bookingCost = bwBooking.bookingCost;
    }
}

