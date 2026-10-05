package com.movieticket;

import com.movieticket.gui.MainFrame;

import javax.swing.*;

/**
 * Main application entry point for Movie Ticket Booking System.
 */
public class Main {
    public static void main(String[] args) {
        // Set System Look & Feel for modern native GUI appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch Swing GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
