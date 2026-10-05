package com.movieticket.gui;

import com.movieticket.model.Movie;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Screen 2: Movie Selection Panel
 */
public class MoviePanel extends JPanel {

    private final JTable tblMovies;
    private final DefaultTableModel tableModel;
    private final List<Movie> movies;

    public interface MovieSelectionListener {
        void onMovieSelected(Movie movie);
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

        JLabel lblStep = new JLabel("STEP 1 OF 4", SwingConstants.LEFT);
        lblStep.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStep.setForeground(UIUtils.COLOR_ACTION_BLUE);

        JLabel lblTitle = new JLabel("Select a Movie", SwingConstants.LEFT);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        pnlHeader.add(lblStep, BorderLayout.NORTH);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Card containing Movies Table
        JPanel pnlCenter = UIUtils.createCardPanel();
        pnlCenter.setLayout(new BorderLayout(0, 15));

        String[] columnNames = {"Movie ID", "Title", "Genre", "Duration (mins)", "Language", "Rating"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Movie m : movies) {
            tableModel.addRow(new Object[]{
                    m.getMovieId(),
                    m.getTitle(),
                    m.getGenre(),
                    m.getDurationMinutes(),
                    m.getLanguage(),
                    m.getRating()
            });
        }

        tblMovies = new JTable(tableModel);
        UIUtils.styleTable(tblMovies);

        // Center align columns except Title
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblMovies.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblMovies.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblMovies.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblMovies.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tblMovies);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // Bottom Actions
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlBottom.setOpaque(false);

        JButton btnSelectMovie = UIUtils.createStyledButton(
                "View Showtimes for Selected Movie ->",
                UIUtils.COLOR_ACTION_INDIGO,
                Color.WHITE,
                UIUtils.FONT_SECTION
        );
        btnSelectMovie.addActionListener(e -> {
            int selectedRow = tblMovies.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a movie row from the table first.", "No Movie Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Movie selectedMovie = movies.get(selectedRow);
            listener.onMovieSelected(selectedMovie);
        });

        pnlBottom.add(btnSelectMovie);
        add(pnlBottom, BorderLayout.SOUTH);
    }
}
