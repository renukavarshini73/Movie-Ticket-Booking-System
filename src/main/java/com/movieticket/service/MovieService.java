package com.movieticket.service;

import com.movieticket.dao.MovieDAO;
import com.movieticket.model.Movie;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for Movie management operations.
 */
public class MovieService {
    private final MovieDAO movieDAO;

    public MovieService() {
        this.movieDAO = new MovieDAO();
    }

    public List<Movie> getAllMovies() throws SQLException {
        return movieDAO.getAllMovies();
    }

    public Movie getMovieById(int movieId) throws SQLException {
        if (movieId <= 0) {
            throw new IllegalArgumentException("Invalid Movie ID.");
        }
        return movieDAO.getMovieById(movieId);
    }
}
