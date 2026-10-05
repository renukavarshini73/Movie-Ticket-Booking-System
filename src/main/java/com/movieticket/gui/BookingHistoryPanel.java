package com.movieticket.gui;

import com.movieticket.model.Booking;
import com.movieticket.service.BookingService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Screen 14: Booking History & Management Reports Panel
 */
public class BookingHistoryPanel extends JPanel {

    private final BookingService bookingService;
    private final JTextField txtSearch;
    private final JTable tblHistory;
    private final DefaultTableModel tableModel;

    private final JLabel lblReportBookings;
    private final JLabel lblReportRevenue;
    private final JLabel lblReportTickets;

    public interface HistoryNavigationListener {
        void onBackToHome();
    }

    public BookingHistoryPanel(HistoryNavigationListener listener) {
        this.bookingService = new BookingService();

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

        JLabel lblTitle = new JLabel("Booking History & Management Reports", SwingConstants.LEFT);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JButton btnHome = UIUtils.createStyledButton(
                "<- Back to Home",
                UIUtils.COLOR_TEXT_MUTED,
                Color.WHITE,
                UIUtils.FONT_BOLD_14
        );
        btnHome.addActionListener(e -> listener.onBackToHome());

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(btnHome, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Area: Search Bar, Report Summary Cards, and Search Table
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setOpaque(false);

        // Top Summary Cards Panel
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 15, 0));
        pnlCards.setOpaque(false);

        lblReportBookings = createReportCard("Total Bookings", "0", UIUtils.COLOR_ACTION_BLUE);
        lblReportRevenue = createReportCard("Total Revenue", "$0.00", UIUtils.COLOR_SEAT_AVAILABLE);
        lblReportTickets = createReportCard("Tickets Sold", "0", UIUtils.COLOR_ACTION_INDIGO);

        pnlCards.add((Component) lblReportBookings.getParent());
        pnlCards.add((Component) lblReportRevenue.getParent());
        pnlCards.add((Component) lblReportTickets.getParent());

        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // Search Bar Panel inside Card Container
        JPanel pnlTableContainer = UIUtils.createCardPanel();
        pnlTableContainer.setLayout(new BorderLayout(0, 15));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        pnlSearch.setOpaque(false);

        JLabel lblQuery = new JLabel("Enter Booking ID or Phone Number:");
        lblQuery.setFont(UIUtils.FONT_SECTION);
        lblQuery.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        txtSearch = new JTextField(18);
        txtSearch.setFont(UIUtils.FONT_PLAIN_14);
        txtSearch.setBorder(new CompoundBorder(new LineBorder(UIUtils.COLOR_BORDER, 1), new EmptyBorder(5, 8, 5, 8)));

        JButton btnSearch = UIUtils.createStyledButton("Search Records", UIUtils.COLOR_ACTION_INDIGO, Color.WHITE, UIUtils.FONT_BOLD_14);
        btnSearch.addActionListener(e -> performSearch());

        JButton btnRefreshStats = UIUtils.createStyledButton("Refresh Report Stats", UIUtils.COLOR_NAVY_HEADER, Color.WHITE, UIUtils.FONT_BOLD_14);
        btnRefreshStats.addActionListener(e -> loadSummaryReport());

        pnlSearch.add(lblQuery);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnRefreshStats);

        pnlTableContainer.add(pnlSearch, BorderLayout.NORTH);

        // Search Results Table
        String[] columnNames = {"Booking ID", "Customer Name", "Phone", "Movie", "Show Date & Time", "Seats", "Total Amount", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHistory = new JTable(tableModel);
        UIUtils.styleTable(tblHistory);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblHistory.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblHistory.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblHistory.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        pnlTableContainer.add(new JScrollPane(tblHistory), BorderLayout.CENTER);

        pnlCenter.add(pnlTableContainer, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // Initial Load
        loadSummaryReport();
    }

    private JLabel createReportCard(String title, String initialValue, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(UIUtils.COLOR_BORDER, 1, true),
                new CompoundBorder(
                        BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor),
                        new EmptyBorder(12, 15, 12, 15)
                )
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblValue = new JLabel(initialValue);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValue.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return lblValue;
    }

    private void loadSummaryReport() {
        try {
            String[] stats = bookingService.getSummaryReport();
            lblReportBookings.setText(stats[0]);
            lblReportRevenue.setText(stats[1]);
            lblReportTickets.setText(stats[2]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load summary statistics: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        String query = txtSearch.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Booking ID or Phone Number to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0); // Clear table
        try {
            List<Booking> results = bookingService.searchBookings(query);
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No matching bookings found for: " + query, "No Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Booking b : results) {
                tableModel.addRow(new Object[]{
                        b.getBookingId(),
                        b.getCustomer() != null ? b.getCustomer().getName() : "N/A",
                        b.getCustomer() != null ? b.getCustomer().getPhone() : "N/A",
                        b.getShow() != null ? b.getShow().getMovieTitle() : "N/A",
                        b.getShow() != null ? (b.getShow().getShowDate() + " @ " + b.getShow().getShowTime()) : "N/A",
                        b.getSeatNumbersFormatted(),
                        String.format("$%.2f", b.getTotalAmount()),
                        b.getBookingStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
