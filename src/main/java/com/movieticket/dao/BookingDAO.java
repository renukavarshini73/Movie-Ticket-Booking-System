package com.movieticket.dao;

import com.movieticket.model.Booking;
import com.movieticket.model.Customer;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import com.movieticket.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for BOOKING and BOOKING_SEAT tables.
 */
public class BookingDAO {

    /**
     * Inserts a Booking row within a JDBC transaction.
     */
    public int createBooking(Booking booking, Connection conn) throws SQLException {
        String sql = "INSERT INTO booking (customer_id, show_id, total_amount, booking_status) VALUES (?, ?, ?, ?)";
        String[] generatedColumns = {"BOOKING_ID"};

        try (PreparedStatement ps = conn.prepareStatement(sql, generatedColumns)) {
            ps.setInt(1, booking.getCustomerId());
            ps.setInt(2, booking.getShowId());
            ps.setDouble(3, booking.getTotalAmount());
            ps.setString(4, booking.getBookingStatus() != null ? booking.getBookingStatus() : "CONFIRMED");

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Creating booking record failed, no ID obtained.");
    }

    /**
     * Inserts reserved seats into BOOKING_SEAT table within a JDBC transaction.
     * Enforces Oracle (show_id, seat_id) PK/UNIQUE constraint.
     */
    public void insertBookingSeats(int showId, int bookingId, List<Seat> seats, Connection conn) throws SQLException {
        String sql = "INSERT INTO booking_seat (show_id, seat_id, booking_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Seat seat : seats) {
                ps.setInt(1, showId);
                ps.setInt(2, seat.getSeatId());
                ps.setInt(3, bookingId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /**
     * Searches bookings by Booking ID or Customer Phone Number.
     */
    public List<Booking> searchBookings(String searchQuery) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.customer_id, b.show_id, b.booking_date, b.total_amount, b.booking_status, " +
                     "       c.name AS customer_name, c.phone AS customer_phone, c.email AS customer_email, " +
                     "       m.title AS movie_title, sc.screen_name, s.show_date, s.show_time, s.ticket_price " +
                     "FROM booking b " +
                     "JOIN customer c ON b.customer_id = c.customer_id " +
                     "JOIN show s ON b.show_id = s.show_id " +
                     "JOIN movie m ON s.movie_id = m.movie_id " +
                     "JOIN screen sc ON s.screen_id = sc.screen_id " +
                     "WHERE TO_CHAR(b.booking_id) = ? OR c.phone = ? " +
                     "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, searchQuery.trim());
            ps.setString(2, searchQuery.trim());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking(
                            rs.getInt("booking_id"),
                            rs.getInt("customer_id"),
                            rs.getInt("show_id"),
                            rs.getTimestamp("booking_date"),
                            rs.getDouble("total_amount"),
                            rs.getString("booking_status")
                    );

                    Customer customer = new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("customer_name"),
                            rs.getString("customer_phone"),
                            rs.getString("customer_email")
                    );
                    booking.setCustomer(customer);

                    Show show = new Show(
                            rs.getInt("show_id"),
                            0,
                            0,
                            rs.getDate("show_date"),
                            rs.getString("show_time"),
                            rs.getDouble("ticket_price")
                    );
                    show.setMovieTitle(rs.getString("movie_title"));
                    show.setScreenName(rs.getString("screen_name"));
                    booking.setShow(show);

                    // Fetch seats for this booking
                    booking.setBookedSeats(getSeatsForBooking(booking.getBookingId(), conn));

                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }

    private List<Seat> getSeatsForBooking(int bookingId, Connection conn) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT s.seat_id, s.screen_id, s.seat_number, s.seat_type " +
                     "FROM seat s " +
                     "JOIN booking_seat bs ON s.seat_id = bs.seat_id " +
                     "WHERE bs.booking_id = ? " +
                     "ORDER BY s.seat_number";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(new Seat(
                            rs.getInt("seat_id"),
                            rs.getInt("screen_id"),
                            rs.getString("seat_number"),
                            rs.getString("seat_type")
                    ));
                }
            }
        }
        return seats;
    }

    /**
     * Retrieves management summary statistics.
     * @return String array: [Total Bookings, Total Revenue, Tickets Sold]
     */
    public String[] getSummaryReport() throws SQLException {
        String sql = "SELECT " +
                     "    COUNT(DISTINCT b.booking_id) AS total_bookings, " +
                     "    NVL(SUM(b.total_amount), 0) AS total_revenue, " +
                     "    COUNT(bs.seat_id) AS tickets_sold " +
                     "FROM booking b " +
                     "LEFT JOIN booking_seat bs ON b.booking_id = bs.booking_id AND b.show_id = bs.show_id " +
                     "WHERE b.booking_status = 'CONFIRMED'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new String[]{
                        String.valueOf(rs.getInt("total_bookings")),
                        String.format("$%.2f", rs.getDouble("total_revenue")),
                        String.valueOf(rs.getInt("tickets_sold"))
                };
            }
        }
        return new String[]{"0", "$0.00", "0"};
    }
}
