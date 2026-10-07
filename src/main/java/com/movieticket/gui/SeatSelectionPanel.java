package com.movieticket.gui;

import com.movieticket.model.Seat;
import com.movieticket.model.Show;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Screen 4: Visual Seat Selection Panel.
 * Features an auditorium screen indicator, color-coded seat buttons grid, legend,
 * ticket pricing in ₹, and live-updating summary card.
 */
public class SeatSelectionPanel extends JPanel {

    private final Show show;
    private final List<Seat> allSeats;
    private final List<Seat> selectedSeats = new ArrayList<>();

    private final JLabel lblSelectedSeatsInfo;
    private final JLabel lblTotalAmountInfo;
    private final JButton btnProceed;

    public interface SeatSelectionListener {
        void onProceedToCustomerDetails(Show show, List<Seat> selectedSeats, double totalAmount);
        void onBackToShows();
    }

    public SeatSelectionPanel(Show show, List<Seat> allSeats, SeatSelectionListener listener) {
        this.show = show;
        this.allSeats = allSeats;

        setLayout(new BorderLayout(0, 15));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setBackground(UIUtils.COLOR_BG_LIGHT);

        // Top Header Banner
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIUtils.COLOR_NAVY_HEADER);
        pnlHeader.setBorder(new CompoundBorder(
                new LineBorder(UIUtils.COLOR_NAVY_HEADER.darker(), 1, true),
                new EmptyBorder(16, 25, 16, 25)
        ));

        JLabel lblStep = new JLabel("STEP 3 OF 4 — INTERACTIVE SEAT ALLOCATION", SwingConstants.LEFT);
        lblStep.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStep.setForeground(UIUtils.COLOR_ACTION_BLUE);

        JLabel lblTitle = new JLabel("Select Your Seats - " + show.getMovieTitle(), SwingConstants.LEFT);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("📅 Date: " + show.getShowDate() + "  |  ⏰ Time: " + show.getShowTime() + "  |  🏛️ " + show.getScreenName() + "  |  💵 " + UIUtils.formatCurrency(show.getTicketPrice()) + " / ticket");
        lblSubtitle.setFont(UIUtils.FONT_SUBTITLE);
        lblSubtitle.setForeground(new Color(203, 213, 225));

        pnlHeader.add(lblStep, BorderLayout.NORTH);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        pnlHeader.add(lblSubtitle, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Area: Screen Banner + Seat Map Container
        JPanel pnlCenter = UIUtils.createCardPanel();
        pnlCenter.setLayout(new BorderLayout(0, 15));

        // Auditorium Screen Indicator Bar
        JLabel lblScreen = new JLabel("🎬   S C R E E N   T H I S   W A Y   🎬", SwingConstants.CENTER);
        lblScreen.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblScreen.setOpaque(true);
        lblScreen.setBackground(new Color(51, 65, 85)); // Slate 700
        lblScreen.setForeground(new Color(241, 245, 249));
        lblScreen.setBorder(new CompoundBorder(
                new LineBorder(new Color(148, 163, 184), 1, true),
                new EmptyBorder(10, 0, 10, 0)
        ));
        pnlCenter.add(lblScreen, BorderLayout.NORTH);

        // Seat Grid (5 columns per row: A1..A5, B1..B5, C1..C5, D1..D5)
        JPanel pnlGrid = new JPanel(new GridLayout(0, 5, 12, 12));
        pnlGrid.setOpaque(false);
        pnlGrid.setBorder(new EmptyBorder(15, 30, 15, 30));

        for (Seat seat : allSeats) {
            JButton btnSeat = new JButton(seat.getSeatNumber());
            btnSeat.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnSeat.setFocusPainted(false);
            btnSeat.setOpaque(true);
            btnSeat.setContentAreaFilled(true);
            btnSeat.setBorderPainted(true);
            btnSeat.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (seat.isBooked()) {
                btnSeat.setBackground(UIUtils.COLOR_SEAT_BOOKED);
                btnSeat.setForeground(Color.WHITE);
                btnSeat.setEnabled(false);
                btnSeat.setToolTipText("Seat " + seat.getSeatNumber() + " is Already Booked");
                btnSeat.setBorder(new LineBorder(UIUtils.COLOR_SEAT_BOOKED.darker(), 1));
            } else {
                btnSeat.setBackground(UIUtils.COLOR_SEAT_AVAILABLE);
                btnSeat.setForeground(Color.WHITE);
                btnSeat.setBorder(new LineBorder(UIUtils.COLOR_SEAT_AVAILABLE.darker(), 1));

                btnSeat.addActionListener(e -> {
                    if (selectedSeats.contains(seat)) {
                        selectedSeats.remove(seat);
                        btnSeat.setBackground(UIUtils.COLOR_SEAT_AVAILABLE);
                        btnSeat.setForeground(Color.WHITE);
                        btnSeat.setBorder(new LineBorder(UIUtils.COLOR_SEAT_AVAILABLE.darker(), 1));
                    } else {
                        selectedSeats.add(seat);
                        btnSeat.setBackground(UIUtils.COLOR_SEAT_SELECTED);
                        btnSeat.setForeground(UIUtils.COLOR_PRIMARY_DARK); // Dark text on yellow
                        btnSeat.setBorder(new LineBorder(UIUtils.COLOR_SEAT_SELECTED.darker(), 2));
                    }
                    updateSummaryBar();
                });
            }
            pnlGrid.add(btnSeat);
        }

        JScrollPane scrollGrid = new JScrollPane(pnlGrid);
        scrollGrid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIUtils.COLOR_BORDER),
                " Auditorium Seat Map ",
                0, 0, UIUtils.FONT_SECTION, UIUtils.COLOR_TEXT_PRIMARY
        ));
        scrollGrid.setOpaque(false);
        scrollGrid.getViewport().setOpaque(false);
        pnlCenter.add(scrollGrid, BorderLayout.CENTER);

        // Color Legend Bar with ● bullet indicators
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 8));
        pnlLegend.setOpaque(false);
        pnlLegend.add(createLegendSwatch("● Available", UIUtils.COLOR_SEAT_AVAILABLE));
        pnlLegend.add(createLegendSwatch("● Selected", UIUtils.COLOR_SEAT_SELECTED));
        pnlLegend.add(createLegendSwatch("● Already Booked", UIUtils.COLOR_SEAT_BOOKED));
        pnlCenter.add(pnlLegend, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        // Bottom Summary Bar & Action Buttons
        JPanel pnlBottom = new JPanel(new BorderLayout(15, 10));
        pnlBottom.setOpaque(false);
        pnlBottom.setBorder(new CompoundBorder(
                new LineBorder(UIUtils.COLOR_BORDER, 1, true),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JPanel pnlInfo = new JPanel(new GridLayout(2, 1, 4, 4));
        pnlInfo.setOpaque(false);

        lblSelectedSeatsInfo = new JLabel("Selected Seats: None");
        lblSelectedSeatsInfo.setFont(UIUtils.FONT_SECTION);
        lblSelectedSeatsInfo.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        lblTotalAmountInfo = new JLabel("Total Amount: " + UIUtils.formatCurrency(0));
        lblTotalAmountInfo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTotalAmountInfo.setForeground(UIUtils.COLOR_ACTION_SUCCESS);

        pnlInfo.add(lblSelectedSeatsInfo);
        pnlInfo.add(lblTotalAmountInfo);
        pnlBottom.add(pnlInfo, BorderLayout.CENTER);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        pnlActions.setOpaque(false);

        JButton btnBack = UIUtils.createStyledButton(
                "← Back to Shows",
                UIUtils.COLOR_TEXT_MUTED,
                Color.WHITE,
                UIUtils.FONT_SECTION
        );
        btnBack.addActionListener(e -> listener.onBackToShows());

        btnProceed = UIUtils.createStyledButton(
                "Proceed to Customer Details ->",
                UIUtils.COLOR_ACTION_INDIGO,
                Color.WHITE,
                UIUtils.FONT_SECTION
        );
        btnProceed.setEnabled(false);
        btnProceed.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat to proceed.", "No Seats Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double total = selectedSeats.size() * show.getTicketPrice();
            listener.onProceedToCustomerDetails(show, selectedSeats, total);
        });

        pnlActions.add(btnBack);
        pnlActions.add(btnProceed);
        pnlBottom.add(pnlActions, BorderLayout.EAST);

        add(pnlBottom, BorderLayout.SOUTH);
    }

    private JPanel createLegendSwatch(String text, Color color) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnl.setOpaque(false);

        JLabel lblColor = new JLabel("       ");
        lblColor.setOpaque(true);
        lblColor.setBackground(color);
        lblColor.setBorder(BorderFactory.createLineBorder(color.darker(), 1));

        JLabel lblText = new JLabel(text);
        lblText.setFont(UIUtils.FONT_BOLD_14);
        lblText.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        pnl.add(lblColor);
        pnl.add(lblText);
        return pnl;
    }

    private void updateSummaryBar() {
        if (selectedSeats.isEmpty()) {
            lblSelectedSeatsInfo.setText("Selected Seats: None");
            lblTotalAmountInfo.setText("Total Amount: " + UIUtils.formatCurrency(0));
            btnProceed.setEnabled(false);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < selectedSeats.size(); i++) {
                sb.append(selectedSeats.get(i).getSeatNumber());
                if (i < selectedSeats.size() - 1) sb.append(", ");
            }
            double total = selectedSeats.size() * show.getTicketPrice();
            lblSelectedSeatsInfo.setText("Selected Seats (" + selectedSeats.size() + "): " + sb.toString());
            lblTotalAmountInfo.setText(String.format("Total Amount: %s (%s / ticket)", UIUtils.formatCurrency(total), UIUtils.formatCurrency(show.getTicketPrice())));
            btnProceed.setEnabled(true);
        }
    }
}
