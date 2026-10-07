package com.movieticket.gui;

import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import com.movieticket.util.ImageLoader;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Screen 3: Showtimes Selection Panel with Movie Poster Preview,
 * showtimes list, ticket prices in ₹, and navigation.
 */
public class ShowPanel extends JPanel {

    private final Movie movie;
    private final List<Show> shows;
    private final JTable tblShows;
    private final DefaultTableModel tableModel;

    public interface ShowSelectionListener {
        void onShowSelected(Show show);
        void onBackToMovies();
    }

    public ShowPanel(Movie movie, List<Show> shows, ShowSelectionListener listener) {
        this.movie = movie;
        this.shows = shows;

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

        JLabel lblStep = new JLabel("STEP 2 OF 4 — SHOWTIME SELECTION", SwingConstants.LEFT);
        lblStep.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStep.setForeground(UIUtils.COLOR_ACTION_BLUE);

        JLabel lblTitle = new JLabel("Select Show Date & Time - " + movie.getTitle(), SwingConstants.LEFT);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        pnlHeader.add(lblStep, BorderLayout.NORTH);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Area: Left Poster Card + Right Shows Table Card
        JPanel pnlCenter = new JPanel(new BorderLayout(20, 0));
        pnlCenter.setOpaque(false);

        // Left Side: Selected Movie Poster Card
        JPanel pnlPosterCard = UIUtils.createCardPanel();
        pnlPosterCard.setLayout(new BorderLayout(0, 12));
        pnlPosterCard.setPreferredSize(new Dimension(240, 0));

        ImageIcon posterIcon = ImageLoader.getMoviePoster(movie.getTitle(), 200, 280);
        JLabel lblPoster = new JLabel(posterIcon, SwingConstants.CENTER);
        lblPoster.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1));
        pnlPosterCard.add(lblPoster, BorderLayout.NORTH);

        JPanel pnlMovieMeta = new JPanel(new GridLayout(4, 1, 4, 4));
        pnlMovieMeta.setOpaque(false);

        JLabel lblMTitle = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        lblMTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblMTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel lblMGenre = new JLabel("🎭 " + movie.getGenre(), SwingConstants.CENTER);
        lblMGenre.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMGenre.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblMTime = new JLabel("⏱️ " + movie.getDurationMinutes() + " mins | " + movie.getLanguage(), SwingConstants.CENTER);
        lblMTime.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMTime.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblMRating = new JLabel("⭐ Rating: " + movie.getRating(), SwingConstants.CENTER);
        lblMRating.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblMRating.setForeground(UIUtils.COLOR_ACTION_INDIGO);

        pnlMovieMeta.add(lblMTitle);
        pnlMovieMeta.add(lblMGenre);
        pnlMovieMeta.add(lblMTime);
        pnlMovieMeta.add(lblMRating);

        pnlPosterCard.add(pnlMovieMeta, BorderLayout.CENTER);
        pnlCenter.add(pnlPosterCard, BorderLayout.WEST);

        // Right Side: Available Shows Table Card
        JPanel pnlTableCard = UIUtils.createCardPanel();
        pnlTableCard.setLayout(new BorderLayout(0, 15));

        JLabel lblTableTitle = new JLabel("Available Showtimes & Screens", SwingConstants.LEFT);
        lblTableTitle.setFont(UIUtils.FONT_SECTION);
        lblTableTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        pnlTableCard.add(lblTableTitle, BorderLayout.NORTH);

        String[] columnNames = {"Show ID", "Show Date", "Show Time", "Auditorium Screen", "Ticket Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Show s : shows) {
            tableModel.addRow(new Object[]{
                    s.getShowId(),
                    s.getShowDate(),
                    s.getShowTime(),
                    s.getScreenName(),
                    UIUtils.formatCurrency(s.getTicketPrice())
            });
        }

        tblShows = new JTable(tableModel);
        UIUtils.styleTable(tblShows);

        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblShows.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblShows.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblShows.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tblShows);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));
        pnlTableCard.add(scrollPane, BorderLayout.CENTER);

        pnlCenter.add(pnlTableCard, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // Bottom Actions
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlBottom.setOpaque(false);

        JButton btnBack = UIUtils.createStyledButton(
                "← Back to Movies",
                UIUtils.COLOR_TEXT_MUTED,
                Color.WHITE,
                UIUtils.FONT_SECTION
        );
        btnBack.addActionListener(e -> listener.onBackToMovies());

        JButton btnSelectShow = UIUtils.createStyledButton(
                "Select Seats for this Show ->",
                UIUtils.COLOR_ACTION_INDIGO,
                Color.WHITE,
                UIUtils.FONT_SECTION
        );
        btnSelectShow.addActionListener(e -> {
            int selectedRow = tblShows.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a showtime row from the table.", "No Show Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Show selectedShow = shows.get(selectedRow);
            listener.onShowSelected(selectedShow);
        });

        pnlBottom.add(btnBack);
        pnlBottom.add(btnSelectShow);
        add(pnlBottom, BorderLayout.SOUTH);
    }
}
