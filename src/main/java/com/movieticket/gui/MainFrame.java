package com.movieticket.gui;

import com.movieticket.model.Booking;
import com.movieticket.model.Customer;
import com.movieticket.model.Movie;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import com.movieticket.service.BookingService;
import com.movieticket.service.MovieService;
import com.movieticket.service.ShowService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Main Application Window managing CardLayout screen navigation across all modules.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainContainer;

    private final MovieService movieService;
    private final ShowService showService;
    private final BookingService bookingService;

    // Navigation Cards Constants
    private static final String CARD_HOME = "HOME";
    private static final String CARD_MOVIES = "MOVIES";
    private static final String CARD_SHOWS = "SHOWS";
    private static final String CARD_SEATS = "SEATS";
    private static final String CARD_CUSTOMER = "CUSTOMER";
    private static final String CARD_HISTORY = "HISTORY";

    public MainFrame() {
        setTitle("Movie Ticket Booking System - Oracle DB / Java Swing");
        setSize(1100, 750);
        setMinimumSize(new Dimension(950, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.movieService = new MovieService();
        this.showService = new ShowService();
        this.bookingService = new BookingService();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(UIUtils.COLOR_BG_LIGHT);

        // Build Home Panel Card
        mainContainer.add(createHomePanel(), CARD_HOME);

        add(mainContainer);

        // Show Home Screen on start
        cardLayout.show(mainContainer, CARD_HOME);
    }

    private JPanel createHomePanel() {
        JPanel pnlHome = new JPanel(new BorderLayout(0, 30));
        pnlHome.setBorder(new EmptyBorder(40, 50, 40, 50));
        pnlHome.setBackground(UIUtils.COLOR_BG_LIGHT);

        // Header Banner Card
        JPanel pnlBanner = new JPanel(new GridLayout(2, 1, 8, 8));
        pnlBanner.setBackground(UIUtils.COLOR_NAVY_HEADER);
        pnlBanner.setBorder(new CompoundBorder(
                new LineBorder(UIUtils.COLOR_NAVY_HEADER.darker(), 1, true),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel lblTitle = new JLabel("🎬 MOVIE TICKET BOOKING SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("Standalone Desktop Application powered by Java Swing & Oracle Database", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblSubtitle.setForeground(new Color(203, 213, 225)); // Slate 300

        pnlBanner.add(lblTitle);
        pnlBanner.add(lblSubtitle);

        pnlHome.add(pnlBanner, BorderLayout.NORTH);

        // Center Content Card with Main Menu Buttons
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setOpaque(false);

        JPanel pnlCard = UIUtils.createCardPanel();
        pnlCard.setLayout(new GridLayout(3, 1, 20, 20));
        pnlCard.setBorder(new EmptyBorder(35, 50, 35, 50));

        // 1. Book Tickets Button
        JButton btnBook = UIUtils.createStyledButton(
                "🎟️  Book Tickets",
                UIUtils.COLOR_ACTION_INDIGO,
                Color.WHITE,
                new Font("SansSerif", Font.BOLD, 17)
        );
        btnBook.setPreferredSize(new Dimension(320, 52));
        btnBook.addActionListener(e -> startBookingFlow());

        // 2. View Bookings Button
        JButton btnViewBookings = UIUtils.createStyledButton(
                "📋  View Bookings",
                UIUtils.COLOR_ACTION_BLUE,
                Color.WHITE,
                new Font("SansSerif", Font.BOLD, 17)
        );
        btnViewBookings.setPreferredSize(new Dimension(320, 52));
        btnViewBookings.addActionListener(e -> openHistoryScreen());

        // 3. Exit Application Button
        JButton btnExit = UIUtils.createStyledButton(
                "❌  Exit Application",
                UIUtils.COLOR_ACTION_DANGER,
                Color.WHITE,
                new Font("SansSerif", Font.BOLD, 17)
        );
        btnExit.setPreferredSize(new Dimension(320, 52));
        btnExit.addActionListener(e -> System.exit(0));

        pnlCard.add(btnBook);
        pnlCard.add(btnViewBookings);
        pnlCard.add(btnExit);

        pnlCenter.add(pnlCard);
        pnlHome.add(pnlCenter, BorderLayout.CENTER);

        // Bottom Footer Highlights
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        pnlFooter.setOpaque(false);

        JLabel lblBadge1 = new JLabel("✓ 3NF Normalized Database");
        lblBadge1.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblBadge1.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblBadge2 = new JLabel("✓ Secure JDBC Transactions");
        lblBadge2.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblBadge2.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblBadge3 = new JLabel("✓ Duplicate Seat Protection");
        lblBadge3.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblBadge3.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblBadge4 = new JLabel("✓ Oracle Database");
        lblBadge4.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblBadge4.setForeground(UIUtils.COLOR_TEXT_MUTED);

        pnlFooter.add(lblBadge1);
        pnlFooter.add(lblBadge2);
        pnlFooter.add(lblBadge3);
        pnlFooter.add(lblBadge4);

        pnlHome.add(pnlFooter, BorderLayout.SOUTH);

        return pnlHome;
    }

    private void startBookingFlow() {
        try {
            List<Movie> movies = movieService.getAllMovies();
            if (movies.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No movies currently found in the database.", "No Movies Available", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            MoviePanel moviePanel = new MoviePanel(movies, new MoviePanel.MovieSelectionListener() {
                @Override
                public void onMovieSelected(Movie selectedMovie) {
                    loadShowtimesScreen(selectedMovie);
                }

                @Override
                public void onBackToHome() {
                    cardLayout.show(mainContainer, CARD_HOME);
                }
            });

            mainContainer.add(moviePanel, CARD_MOVIES);
            cardLayout.show(mainContainer, CARD_MOVIES);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadShowtimesScreen(Movie selectedMovie) {
        try {
            List<Show> shows = showService.getShowsForMovie(selectedMovie.getMovieId());
            if (shows.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No scheduled shows available for '" + selectedMovie.getTitle() + "'.", "No Shows Scheduled", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            ShowPanel showPanel = new ShowPanel(selectedMovie, shows, new ShowPanel.ShowSelectionListener() {
                @Override
                public void onShowSelected(Show selectedShow) {
                    loadSeatSelectionScreen(selectedShow);
                }

                @Override
                public void onBackToMovies() {
                    cardLayout.show(mainContainer, CARD_MOVIES);
                }
            });

            mainContainer.add(showPanel, CARD_SHOWS);
            cardLayout.show(mainContainer, CARD_SHOWS);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load shows: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSeatSelectionScreen(Show selectedShow) {
        try {
            List<Seat> seats = showService.getSeatsForShow(selectedShow);

            SeatSelectionPanel seatPanel = new SeatSelectionPanel(selectedShow, seats, new SeatSelectionPanel.SeatSelectionListener() {
                @Override
                public void onProceedToCustomerDetails(Show show, List<Seat> selectedSeats, double totalAmount) {
                    loadCustomerDetailsScreen(show, selectedSeats, totalAmount);
                }

                @Override
                public void onBackToShows() {
                    cardLayout.show(mainContainer, CARD_SHOWS);
                }
            });

            mainContainer.add(seatPanel, CARD_SEATS);
            cardLayout.show(mainContainer, CARD_SEATS);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load seat availability: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCustomerDetailsScreen(Show show, List<Seat> selectedSeats, double totalAmount) {
        CustomerPanel customerPanel = new CustomerPanel(show, selectedSeats, totalAmount, new CustomerPanel.CustomerFormListener() {
            @Override
            public void onConfirmBooking(Customer customer, Show show, List<Seat> selectedSeats, double totalAmount) {
                showBookingConfirmationDialog(customer, show, selectedSeats, totalAmount);
            }

            @Override
            public void onBackToSeats() {
                cardLayout.show(mainContainer, CARD_SEATS);
            }
        });

        mainContainer.add(customerPanel, CARD_CUSTOMER);
        cardLayout.show(mainContainer, CARD_CUSTOMER);
    }

    private void showBookingConfirmationDialog(Customer customer, Show show, List<Seat> selectedSeats, double totalAmount) {
        StringBuilder seatList = new StringBuilder();
        for (int i = 0; i < selectedSeats.size(); i++) {
            seatList.append(selectedSeats.get(i).getSeatNumber());
            if (i < selectedSeats.size() - 1) seatList.append(", ");
        }

        String message = String.format(
                "Please confirm your booking details:\n\n" +
                "🎬 Movie: %s\n" +
                "📅 Date & Time: %s @ %s\n" +
                "🏛️ Screen: %s\n" +
                "👤 Customer: %s (%s)\n" +
                "✉️ Email: %s\n" +
                "💺 Selected Seats (%d): %s\n" +
                "💰 Total Price: %s\n\n" +
                "Proceed to complete database transaction?",
                show.getMovieTitle(), show.getShowDate(), show.getShowTime(), show.getScreenName(),
                customer.getName(), customer.getPhone(), customer.getEmail(),
                selectedSeats.size(), seatList.toString(), UIUtils.formatCurrency(totalAmount)
        );

        int choice = JOptionPane.showConfirmDialog(
                this,
                message,
                "Confirm Ticket Booking",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            executeBookingTransaction(customer, show, selectedSeats, totalAmount);
        }
    }

    private void executeBookingTransaction(Customer customer, Show show, List<Seat> selectedSeats, double totalAmount) {
        try {
            Booking booking = bookingService.processBooking(customer, show.getShowId(), selectedSeats, totalAmount);

            String receipt = String.format(
                    "=========================================\n" +
                    "       BOOKING CONFIRMED SUCCESSFULLY    \n" +
                    "=========================================\n" +
                    "Booking ID: #%d\n" +
                    "Movie: %s\n" +
                    "Showtime: %s @ %s (%s)\n" +
                    "Customer: %s (%s)\n" +
                    "Seats Allocated: %s\n" +
                    "Total Paid: %s\n" +
                    "=========================================\n" +
                    "Thank you for booking with us!",
                    booking.getBookingId(), show.getMovieTitle(), show.getShowDate(), show.getShowTime(), show.getScreenName(),
                    customer.getName(), customer.getPhone(), booking.getSeatNumbersFormatted(), UIUtils.formatCurrency(totalAmount)
            );

            JOptionPane.showMessageDialog(this, receipt, "Booking Successful!", JOptionPane.INFORMATION_MESSAGE);

            // Navigate back to Home
            cardLayout.show(mainContainer, CARD_HOME);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Booking Failed / Conflict", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openHistoryScreen() {
        BookingHistoryPanel historyPanel = new BookingHistoryPanel(new BookingHistoryPanel.HistoryNavigationListener() {
            @Override
            public void onBackToHome() {
                cardLayout.show(mainContainer, CARD_HOME);
            }

            @Override
            public void onStartBooking() {
                startBookingFlow();
            }
        });
        mainContainer.add(historyPanel, CARD_HISTORY);
        cardLayout.show(mainContainer, CARD_HISTORY);
        historyPanel.loadAllBookings();
    }
}
