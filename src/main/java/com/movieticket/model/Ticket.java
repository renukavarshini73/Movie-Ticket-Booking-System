package com.movieticket.model;

import java.io.Serializable;

/**
 * Model class representing individual Ticket allocation (BOOKING_SEAT junction).
 */
public class Ticket implements Serializable {
    private int showId;
    private int seatId;
    private int bookingId;
    private String seatNumber;
    private String seatType;

    public Ticket() {}

    public Ticket(int showId, int seatId, int bookingId) {
        this.showId = showId;
        this.seatId = seatId;
        this.bookingId = bookingId;
    }

    public Ticket(int showId, int seatId, int bookingId, String seatNumber, String seatType) {
        this.showId = showId;
        this.seatId = seatId;
        this.bookingId = bookingId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
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

    @Override
    public String toString() {
        return "Ticket [Show #" + showId + ", Seat: " + seatNumber + " (" + seatType + "), Booking #" + bookingId + "]";
    }
}
