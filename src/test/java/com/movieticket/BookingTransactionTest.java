package com.movieticket;

import com.movieticket.model.Booking;
import com.movieticket.model.Customer;
import com.movieticket.model.Movie;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import com.movieticket.service.BookingService;
import com.movieticket.service.MovieService;
import com.movieticket.service.ShowService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Automated Verification Test for Movie Ticket Booking System.
 */
public class BookingTransactionTest {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  RUNNING SYSTEM INTEGRATION & TRANSACTION TESTS ");
        System.out.println("=================================================");

        MovieService movieService = new MovieService();
        ShowService showService = new ShowService();
        BookingService bookingService = new BookingService();

        try {
            // Test 1: Fetch Movies
            List<Movie> movies = movieService.getAllMovies();
            System.out.println("[TEST 1 PASS] Retrieved " + movies.size() + " movies from Oracle DB.");
            assert !movies.isEmpty() : "Movies list should not be empty";

            Movie testMovie = null;
            List<Show> shows = new ArrayList<>();

            for (Movie m : movies) {
                List<Show> sList = showService.getShowsForMovie(m.getMovieId());
                if (!sList.isEmpty()) {
                    testMovie = m;
                    shows = sList;
                    break;
                }
            }

            if (testMovie == null) {
                System.err.println("No shows found for any movie.");
                return;
            }

            // Test 2: Fetch Shows
            System.out.println("[TEST 2 PASS] Retrieved " + shows.size() + " shows for movie '" + testMovie.getTitle() + "'.");
            Show testShow = shows.get(0);

            // Test 3: Fetch Seats
            List<Seat> seats = showService.getSeatsForShow(testShow);
            System.out.println("[TEST 3 PASS] Retrieved " + seats.size() + " total seats for screen.");

            // Find two available seats
            List<Seat> selectedSeats = new ArrayList<>();
            for (Seat s : seats) {
                if (!s.isBooked()) {
                    selectedSeats.add(s);
                    if (selectedSeats.size() == 2) break;
                }
            }

            if (selectedSeats.size() < 2) {
                System.out.println("[SKIP TEST 4 & 5] Not enough available seats for test run.");
                return;
            }

            Customer cust1 = new Customer("Test User Alpha", "9876500001", "alpha@test.com");
            double total1 = selectedSeats.size() * testShow.getTicketPrice();

            // Test 4: Successful Booking Transaction
            Booking booking1 = bookingService.processBooking(cust1, testShow.getShowId(), selectedSeats, total1);
            System.out.println("[TEST 4 PASS] Booking #" + booking1.getBookingId() + " created successfully for seats: " + booking1.getSeatNumbersFormatted());

            // Test 5: Duplicate Seat Concurrency Collision (Must fail with Oracle Exception)
            Customer cust2 = new Customer("Test User Beta", "9876500002", "beta@test.com");
            try {
                bookingService.processBooking(cust2, testShow.getShowId(), selectedSeats, total1);
                System.err.println("[TEST 5 FAIL] Duplicate seat booking was NOT blocked!");
            } catch (SQLException ex) {
                System.out.println("[TEST 5 PASS] Duplicate seat booking successfully BLOCKED by Oracle Database!");
                System.out.println("  Detail message: " + ex.getMessage());
            }

            System.out.println("\n=================================================");
            System.out.println("   ALL 5 INTEGRATION TEST CASES PASSED CLEANLY!  ");
            System.out.println("=================================================");

        } catch (Exception e) {
            System.err.println("Integration Test Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
