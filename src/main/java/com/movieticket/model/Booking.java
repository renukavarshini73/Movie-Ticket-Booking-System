package com.movieticket.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a Ticket Booking transaction.
 */
public class Booking implements Serializable {
    private int bookingId;
    private int customerId;
    private int showId;
    private Timestamp bookingDate;
    private double totalAmount;
    private String bookingStatus;

    // Associated Entities and Display metadata
    private Customer customer;
    private Show show;
    private List<Seat> bookedSeats = new ArrayList<>();

    public Booking() {}

    public Booking(int bookingId, int customerId, int showId, Timestamp bookingDate, double totalAmount, String bookingStatus) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.showId = showId;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public List<Seat> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<Seat> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public void addBookedSeat(Seat seat) {
        this.bookedSeats.add(seat);
    }

    public String getSeatNumbersFormatted() {
        if (bookedSeats == null || bookedSeats.isEmpty()) return "N/A";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bookedSeats.size(); i++) {
            sb.append(bookedSeats.get(i).getSeatNumber());
            if (i < bookedSeats.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Booking #" + bookingId + " - Amount: $" + String.format("%.2f", totalAmount) + " [" + bookingStatus + "]";
    }
}
