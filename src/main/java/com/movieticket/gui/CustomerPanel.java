package com.movieticket.gui;

import com.movieticket.model.Customer;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import com.movieticket.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Screen 6: Customer Contact Form Panel
 */
public class CustomerPanel extends JPanel {

    private final Show show;
    private final List<Seat> selectedSeats;
    private final double totalAmount;

    private final JTextField txtName;
    private final JTextField txtPhone;
    private final JTextField txtEmail;

    public interface CustomerFormListener {
        void onConfirmBooking(Customer customer, Show show, List<Seat> selectedSeats, double totalAmount);
        void onBackToSeats();
    }

    public CustomerPanel(Show show, List<Seat> selectedSeats, double totalAmount, CustomerFormListener listener) {
        this.show = show;
        this.selectedSeats = selectedSeats;
        this.totalAmount = totalAmount;

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

        JLabel lblStep = new JLabel("STEP 4 OF 4 — CUSTOMER INFORMATION", SwingConstants.LEFT);
        lblStep.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStep.setForeground(UIUtils.COLOR_ACTION_BLUE);

        JLabel lblTitle = new JLabel("Enter Contact Details", SwingConstants.LEFT);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        pnlHeader.add(lblStep, BorderLayout.NORTH);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // Form Fields Container
        JPanel pnlForm = UIUtils.createCardPanel();
        pnlForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Booking Summary Box Card
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JTextArea txtSummary = new JTextArea(
                "🎬 Movie: " + show.getMovieTitle() + "\n" +
                "📅 Date & Time: " + show.getShowDate() + " @ " + show.getShowTime() + " (" + show.getScreenName() + ")\n" +
                "💺 Selected Seats (" + selectedSeats.size() + "): " + getSeatNumbersFormatted() + "\n" +
                "💵 Total Amount: " + UIUtils.formatCurrency(totalAmount)
        );
        txtSummary.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtSummary.setEditable(false);
        txtSummary.setBackground(new Color(241, 245, 249));
        txtSummary.setForeground(UIUtils.COLOR_PRIMARY_DARK);
        txtSummary.setBorder(new CompoundBorder(
                new LineBorder(UIUtils.COLOR_BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));
        pnlForm.add(txtSummary, gbc);

        gbc.gridwidth = 1;

        // Row 1: Customer Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblName = new JLabel("Full Name:");
        lblName.setFont(UIUtils.FONT_SECTION);
        lblName.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        pnlForm.add(lblName, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtName = new JTextField(22);
        txtName.setFont(UIUtils.FONT_PLAIN_14);
        txtName.setBorder(new CompoundBorder(new LineBorder(UIUtils.COLOR_BORDER, 1), new EmptyBorder(6, 8, 6, 8)));
        pnlForm.add(txtName, gbc);

        // Row 2: Phone Number
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel lblPhone = new JLabel("Phone Number:");
        lblPhone.setFont(UIUtils.FONT_SECTION);
        lblPhone.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        pnlForm.add(lblPhone, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPhone = new JTextField(22);
        txtPhone.setFont(UIUtils.FONT_PLAIN_14);
        txtPhone.setToolTipText("Enter 10 to 15 digit phone number");
        txtPhone.setBorder(new CompoundBorder(new LineBorder(UIUtils.COLOR_BORDER, 1), new EmptyBorder(6, 8, 6, 8)));
        pnlForm.add(txtPhone, gbc);

        // Row 3: Email Address
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        JLabel lblEmail = new JLabel("Email Address:");
        lblEmail.setFont(UIUtils.FONT_SECTION);
        lblEmail.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        pnlForm.add(lblEmail, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEmail = new JTextField(22);
        txtEmail.setFont(UIUtils.FONT_PLAIN_14);
        txtEmail.setToolTipText("e.g. user@domain.com");
        txtEmail.setBorder(new CompoundBorder(new LineBorder(UIUtils.COLOR_BORDER, 1), new EmptyBorder(6, 8, 6, 8)));
        pnlForm.add(txtEmail, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // Bottom Actions
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlBottom.setOpaque(false);

        JButton btnBack = UIUtils.createStyledButton(
                "← Change Seats",
                UIUtils.COLOR_TEXT_MUTED,
                Color.WHITE,
                UIUtils.FONT_SECTION
        );
        btnBack.addActionListener(e -> listener.onBackToSeats());

        JButton btnSubmit = UIUtils.createStyledButton(
                "Review & Confirm Booking ->",
                UIUtils.COLOR_ACTION_SUCCESS,
                Color.WHITE,
                UIUtils.FONT_SECTION
        );
        btnSubmit.addActionListener(e -> {
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();

            try {
                ValidationUtil.validateCustomerInput(name, phone, email);
                Customer customer = new Customer(name, phone, email);
                listener.onConfirmBooking(customer, show, selectedSeats, totalAmount);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlBottom.add(btnBack);
        pnlBottom.add(btnSubmit);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private String getSeatNumbersFormatted() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedSeats.size(); i++) {
            sb.append(selectedSeats.get(i).getSeatNumber());
            if (i < selectedSeats.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
