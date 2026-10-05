package com.movieticket.dao;

import com.movieticket.model.Seat;
import com.movieticket.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data Access Object for SEAT and BOOKING_SEAT queries.
 */
public class SeatDAO {

    public List<Seat> getSeatsByScreenId(int screenId) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT seat_id, screen_id, seat_number, seat_type FROM seat WHERE screen_id = ? ORDER BY seat_number";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, screenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat(
                            rs.getInt("seat_id"),
                            rs.getInt("screen_id"),
                            rs.getString("seat_number"),
                            rs.getString("seat_type")
                    );
                    seats.add(seat);
                }
            }
        }
        return seats;
    }

    public Set<Integer> getBookedSeatIdsForShow(int showId) throws SQLException {
        Set<Integer> bookedSeatIds = new HashSet<>();
        String sql = "SELECT seat_id FROM booking_seat WHERE show_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookedSeatIds.add(rs.getInt("seat_id"));
                }
            }
        }
        return bookedSeatIds;
    }
}
