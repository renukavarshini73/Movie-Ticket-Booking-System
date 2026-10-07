package com.movieticket.service;

import com.movieticket.dao.BookingDAO;
import com.movieticket.dao.CustomerDAO;
import com.movieticket.model.Booking;
import com.movieticket.model.Customer;
import com.movieticket.model.Seat;
import com.movieticket.util.DatabaseConnection;
import com.movieticket.util.ValidationUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Service class for executing atomic JDBC Booking Transactions.
 */
public class BookingService {
    private final CustomerDAO customerDAO;
    private final BookingDAO bookingDAO;

    public BookingService() {
        this.customerDAO = new CustomerDAO();
        this.bookingDAO = new BookingDAO();
    }

    /**
     * Processes a ticket booking atomically using a JDBC transaction.
     * Enforces (show_id, seat_id) uniqueness at the database level.
     */
    public Booking processBooking(Customer customer, int showId, List<Seat> selectedSeats, double totalAmount) throws SQLException {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            throw new IllegalArgumentException("At least one seat must be selected for booking.");
        }

        ValidationUtil.validateCustomerInput(customer.getName(), customer.getPhone(), customer.getEmail());

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // 1. Start JDBC Transaction

            // 2. Insert or retrieve Customer ID
            int customerId = customerDAO.insertOrGetCustomer(customer, conn);
            customer.setCustomerId(customerId);

            // 3. Create Booking Header Record
            Booking booking = new Booking();
            booking.setCustomerId(customerId);
            booking.setShowId(showId);
            booking.setTotalAmount(totalAmount);
            booking.setBookingStatus("CONFIRMED");

            int bookingId = bookingDAO.createBooking(booking, conn);
            booking.setBookingId(bookingId);
            booking.setCustomer(customer);
            booking.setBookedSeats(selectedSeats);

            // 4. Insert Reserved Seats into BOOKING_SEAT
            // If any seat is already booked for this show, Oracle triggers ORA-00001
            bookingDAO.insertBookingSeats(showId, bookingId, selectedSeats, conn);

            // 5. Commit Transaction
            conn.commit();
            return booking;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback all changes if any error occurs
                } catch (SQLException ex) {
                    System.err.println("[BookingService] Transaction Rollback Failed: " + ex.getMessage());
                }
            }

            // ORA-00001: Unique constraint violated (Duplicate seat allocation)
            if (e.getErrorCode() == 1 || (e.getMessage() != null && e.getMessage().contains("PK_BOOKING_SEAT"))) {
                throw new SQLException("Seat Booking Conflict: One or more selected seats have already been booked by another user for this show.");
            }
            throw e;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[BookingService] Connection Close Exception: " + e.getMessage());
                }
            }
        }
    }

    public List<Booking> getAllBookings() throws SQLException {
        return bookingDAO.getAllBookings();
    }

    public List<Booking> searchBookings(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty.");
        }
        return bookingDAO.searchBookings(query);
    }

    public String[] getSummaryReport() throws SQLException {
        return bookingDAO.getSummaryReport();
    }
}
