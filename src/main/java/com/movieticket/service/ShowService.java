package com.movieticket.service;

import com.movieticket.dao.SeatDAO;
import com.movieticket.dao.ShowDAO;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Service class for Show schedule and Seat layout retrieval.
 */
public class ShowService {
    private final ShowDAO showDAO;
    private final SeatDAO seatDAO;

    public ShowService() {
        this.showDAO = new ShowDAO();
        this.seatDAO = new SeatDAO();
    }

    public List<Show> getShowsForMovie(int movieId) throws SQLException {
        if (movieId <= 0) {
            throw new IllegalArgumentException("Invalid Movie ID.");
        }
        return showDAO.getShowsByMovieId(movieId);
    }

    public Show getShowById(int showId) throws SQLException {
        if (showId <= 0) {
            throw new IllegalArgumentException("Invalid Show ID.");
        }
        return showDAO.getShowById(showId);
    }

    /**
     * Retrieves seats for a show screen and marks already-booked seats.
     */
    public List<Seat> getSeatsForShow(Show show) throws SQLException {
        if (show == null) {
            throw new IllegalArgumentException("Show cannot be null.");
        }

        List<Seat> seats = seatDAO.getSeatsByScreenId(show.getScreenId());
        Set<Integer> bookedSeatIds = seatDAO.getBookedSeatIdsForShow(show.getShowId());

        for (Seat seat : seats) {
            if (bookedSeatIds.contains(seat.getSeatId())) {
                seat.setBooked(true);
            }
        }
        return seats;
    }
}
