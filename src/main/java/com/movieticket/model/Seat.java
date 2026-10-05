package com.movieticket.model;

import java.io.Serializable;

/**
 * Model class representing a physical seat inside a screen.
 */
public class Seat implements Serializable {
    private int seatId;
    private int screenId;
    private String seatNumber;
    private String seatType; // STANDARD, PREMIUM, VIP
    private boolean isBooked; // Runtime state for Swing seat selection grid

    public Seat() {}

    public Seat(int seatId, int screenId, String seatNumber, String seatType) {
        this.seatId = seatId;
        this.screenId = screenId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.isBooked = false;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return seatNumber + " [" + seatType + "]";
    }
}
