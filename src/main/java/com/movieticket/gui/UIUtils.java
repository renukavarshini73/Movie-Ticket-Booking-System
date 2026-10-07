package com.movieticket.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * UI Utilities providing consistent color tokens, typography, currency formatting in ₹,
 * custom button styling, and layout helpers across the entire Swing application.
 */
public class UIUtils {

    // Currency Symbol for Indian Rupees
    public static final String CURRENCY_SYMBOL = "₹";

    // Color Palette
    public static final Color COLOR_PRIMARY_DARK = new Color(15, 23, 42);   // #0f172a Slate 900
    public static final Color COLOR_NAVY_HEADER = new Color(30, 41, 59);   // #1e293b Slate 800
    public static final Color COLOR_BG_LIGHT = new Color(248, 250, 252);    // #f8fafc Slate 50
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(226, 232, 240);    // #e2e8f0 Slate 200

    // Action Colors
    public static final Color COLOR_ACTION_INDIGO = new Color(79, 70, 229); // #4f46e5 Primary Action
    public static final Color COLOR_ACTION_BLUE = new Color(2, 132, 199);   // #0284c7 Secondary Action
    public static final Color COLOR_ACTION_SUCCESS = new Color(16, 185, 129);// #10b981 Success
    public static final Color COLOR_ACTION_DANGER = new Color(220, 38, 38);  // #dc2626 Danger

    // Seat Status Colors
    public static final Color COLOR_SEAT_AVAILABLE = new Color(34, 197, 94); // #22c55e Emerald Green
    public static final Color COLOR_SEAT_SELECTED = new Color(234, 179, 8);  // #eab308 Amber Yellow
    public static final Color COLOR_SEAT_BOOKED = new Color(239, 68, 68);   // #ef4444 Rose Red

    // Text Colors
    public static final Color COLOR_TEXT_PRIMARY = new Color(15, 23, 42);  // #0f172a
    public static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);  // #64748b

    // Fonts
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SECTION = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONT_BOLD_14 = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_PLAIN_14 = new Font("SansSerif", Font.PLAIN, 14);

    /**
     * Formats amount in Indian Rupees (₹).
     */
    public static String formatCurrency(double amount) {
        return String.format("%s%.2f", CURRENCY_SYMBOL, amount);
    }

    /**
     * Creates a custom styled button that renders reliably across all Swing Look and Feels.
     */
    public static JButton createStyledButton(String text, Color bg, Color fg, Font font) {
        JButton button = new JButton(text);
        button.setFont(font != null ? font : FONT_BOLD_14);
        button.setForeground(fg != null ? fg : Color.WHITE);
        button.setBackground(bg != null ? bg : COLOR_ACTION_INDIGO);
        
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Border lineBorder = new LineBorder(bg.darker(), 1, true);
        Border marginBorder = new EmptyBorder(8, 18, 8, 18);
        button.setBorder(new CompoundBorder(lineBorder, marginBorder));

        return button;
    }

    /**
     * Styles a JTable header and rows for clean presentation.
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_PLAIN_14);
        table.setRowHeight(36);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(COLOR_BORDER);
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        table.getTableHeader().setFont(FONT_SECTION);
        table.getTableHeader().setBackground(COLOR_NAVY_HEADER);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width, 38));
        table.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Creates a styled card container panel with a border.
     */
    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));
        return card;
    }
}
