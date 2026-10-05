package com.movieticket.dao;

import com.movieticket.model.Show;
import com.movieticket.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for SHOW table.
 */
public class ShowDAO {

    public List<Show> getShowsByMovieId(int movieId) throws SQLException {
        List<Show> shows = new ArrayList<>();
        String sql = "SELECT s.show_id, s.movie_id, s.screen_id, s.show_date, s.show_time, s.ticket_price, " +
                     "       m.title AS movie_title, sc.screen_name " +
                     "FROM show s " +
                     "JOIN movie m ON s.movie_id = m.movie_id " +
                     "JOIN screen sc ON s.screen_id = sc.screen_id " +
                     "WHERE s.movie_id = ? " +
                     "ORDER BY s.show_date, s.show_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Show show = new Show(
                            rs.getInt("show_id"),
                            rs.getInt("movie_id"),
                            rs.getInt("screen_id"),
                            rs.getDate("show_date"),
                            rs.getString("show_time"),
                            rs.getDouble("ticket_price")
                    );
                    show.setMovieTitle(rs.getString("movie_title"));
                    show.setScreenName(rs.getString("screen_name"));
                    shows.add(show);
                }
            }
        }
        return shows;
    }

    public Show getShowById(int showId) throws SQLException {
        String sql = "SELECT s.show_id, s.movie_id, s.screen_id, s.show_date, s.show_time, s.ticket_price, " +
                     "       m.title AS movie_title, sc.screen_name " +
                     "FROM show s " +
                     "JOIN movie m ON s.movie_id = m.movie_id " +
                     "JOIN screen sc ON s.screen_id = sc.screen_id " +
                     "WHERE s.show_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, showId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Show show = new Show(
                            rs.getInt("show_id"),
                            rs.getInt("movie_id"),
                            rs.getInt("screen_id"),
                            rs.getDate("show_date"),
                            rs.getString("show_time"),
                            rs.getDouble("ticket_price")
                    );
                    show.setMovieTitle(rs.getString("movie_title"));
                    show.setScreenName(rs.getString("screen_name"));
                    return show;
                }
            }
        }
        return null;
    }
}
