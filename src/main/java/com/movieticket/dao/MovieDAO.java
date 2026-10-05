package com.movieticket.dao;

import com.movieticket.model.Movie;
import com.movieticket.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for MOVIE table.
 */
public class MovieDAO {

    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT movie_id, title, genre, duration_minutes, language, rating FROM movie ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("movie_id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("duration_minutes"),
                        rs.getString("language"),
                        rs.getString("rating")
                );
                movies.add(movie);
            }
        }
        return movies;
    }

    public Movie getMovieById(int movieId) throws SQLException {
        String sql = "SELECT movie_id, title, genre, duration_minutes, language, rating FROM movie WHERE movie_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                            rs.getInt("movie_id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getInt("duration_minutes"),
                            rs.getString("language"),
                            rs.getString("rating")
                    );
                }
            }
        }
        return null;
    }
}
