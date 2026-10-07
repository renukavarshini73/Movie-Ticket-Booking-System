package com.movieticket.gui;

import com.movieticket.model.Movie;
import com.movieticket.util.ImageLoader;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Screen 2: Movie Selection Panel displaying visual Movie Cards with Poster Images,
 * ratings, duration, pricing in ₹, and "← Back to Home" navigation.
 */
public class MoviePanel extends JPanel {

    private final List<Movie> movies;

    public interface MovieSelectionListener {
        void onMovieSelected(Movie movie);
        void onBackToHome();
    }

    public MoviePanel(List<Movie> movies, MovieSelectionListener listener) {
        this.movies = movies;

        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(25, 30, 25, 30));
        setBackground(UIUtils.COLOR_BG_LIGHT);

        // Header Banner Card
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIUtils.COLOR_NAVY_HEADER);
        pnlHeader.setBorder(new CompoundBorder(
                new LineBorder(UIUtils.COLOR_NAVY_HEADER.darker(), 1, true),
                new EmptyBorder(18, 25, 18, 25)
        ));

        JPanel pnlHeaderLeft = new JPanel(new GridLayout(2, 1, 4, 4));
        pnlHeaderLeft.setOpaque(false);

        JLabel lblStep = new JLabel("STEP 1 OF 4 — MOVIE SELECTION", SwingConstants.LEFT);
        lblStep.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStep.setForeground(UIUtils.COLOR_ACTION_BLUE);

        JLabel lblTitle = new JLabel("Select a Movie", SwingConstants.LEFT);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        pnlHeaderLeft.add(lblStep);
        pnlHeaderLeft.add(lblTitle);

        // "← Back to Home" Button in Top Header
        JButton btnBackHome = UIUtils.createStyledButton(
                "← Back to Home",
                UIUtils.COLOR_TEXT_MUTED,
                Color.WHITE,
                UIUtils.FONT_BOLD_14
        );
        btnBackHome.addActionListener(e -> listener.onBackToHome());

        pnlHeader.add(pnlHeaderLeft, BorderLayout.WEST);
        pnlHeader.add(btnBackHome, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // Movie Cards Grid (3 Columns)
        JPanel pnlCardsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        pnlCardsGrid.setOpaque(false);
        pnlCardsGrid.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (Movie m : movies) {
            pnlCardsGrid.add(createMovieCard(m, listener));
        }

        JScrollPane scrollPane = new JScrollPane(pnlCardsGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMovieCard(Movie movie, MovieSelectionListener listener) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UIUtils.COLOR_CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(UIUtils.COLOR_BORDER, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        // Poster Image (160x220)
        ImageIcon posterIcon = ImageLoader.getMoviePoster(movie.getTitle(), 160, 220);
        JLabel lblPoster = new JLabel(posterIcon, SwingConstants.CENTER);
        lblPoster.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1));
        card.add(lblPoster, BorderLayout.NORTH);

        // Movie Information Center
        JPanel pnlInfo = new JPanel(new GridLayout(4, 1, 3, 3));
        pnlInfo.setOpaque(false);

        JLabel lblMovieTitle = new JLabel(movie.getTitle(), SwingConstants.LEFT);
        lblMovieTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblMovieTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel lblGenre = new JLabel("🎭 " + movie.getGenre(), SwingConstants.LEFT);
        lblGenre.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblGenre.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblDetails = new JLabel("⏱️ " + movie.getDurationMinutes() + " mins  |  🌐 " + movie.getLanguage() + "  |  ⭐ " + movie.getRating(), SwingConstants.LEFT);
        lblDetails.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDetails.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblPriceTag = new JLabel("Ticket Starting at: " + UIUtils.CURRENCY_SYMBOL + "180 - " + UIUtils.CURRENCY_SYMBOL + "280", SwingConstants.LEFT);
        lblPriceTag.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPriceTag.setForeground(UIUtils.COLOR_ACTION_SUCCESS);

        pnlInfo.add(lblMovieTitle);
        pnlInfo.add(lblGenre);
        pnlInfo.add(lblDetails);
        pnlInfo.add(lblPriceTag);

        card.add(pnlInfo, BorderLayout.CENTER);

        // Book Now Action Button
        JButton btnBookNow = UIUtils.createStyledButton(
                "Book Showtimes ->",
                UIUtils.COLOR_ACTION_INDIGO,
                Color.WHITE,
                UIUtils.FONT_BOLD_14
        );
        btnBookNow.addActionListener(e -> listener.onMovieSelected(movie));
        card.add(btnBookNow, BorderLayout.SOUTH);

        return card;
    }
}
