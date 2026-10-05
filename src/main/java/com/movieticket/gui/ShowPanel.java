package com.movieticket.gui;

import com.movieticket.model.Movie;
import com.movieticket.model.Show;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Screen 3: Showtimes Selection Panel
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

        JLabel lblStep = new JLabel("STEP 2 OF 4", SwingConstants.LEFT);
        lblStep.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStep.setForeground(UIUtils.COLOR_ACTION_BLUE);

        JLabel lblTitle = new JLabel("Select Show Date & Time - " + movie.getTitle(), SwingConstants.LEFT);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Genre: " + movie.getGenre() + " | Duration: " + movie.getDurationMinutes() + " mins | Language: " + movie.getLanguage());
        lblSub.setFont(UIUtils.FONT_SUBTITLE);
        lblSub.setForeground(new Color(203, 213, 225));

        pnlHeader.add(lblStep, BorderLayout.NORTH);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        pnlHeader.add(lblSub, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Card containing Shows Table
        JPanel pnlCenter = UIUtils.createCardPanel();
        pnlCenter.setLayout(new BorderLayout(0, 15));

        String[] columnNames = {"Show ID", "Date", "Time", "Auditorium Screen", "Ticket Price"};
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
                    String.format("$%.2f", s.getTicketPrice())
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
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // Bottom Actions
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlBottom.setOpaque(false);

        JButton btnBack = UIUtils.createStyledButton(
                "<- Back to Movies",
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
