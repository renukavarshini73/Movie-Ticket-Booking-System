package com.movieticket.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility for loading and scaling local movie poster image resources.
 */
public class ImageLoader {

    private static final Map<String, String> POSTER_MAP = new HashMap<>();

    static {
        POSTER_MAP.put("Inception", "images/inception.png");
        POSTER_MAP.put("Interstellar", "images/interstellar.png");
        POSTER_MAP.put("The Dark Knight", "images/dark_knight.png");
        POSTER_MAP.put("Avatar: The Way of Water", "images/avatar.png");
        POSTER_MAP.put("Pushpa 2: The Rule", "images/pushpa2.png");
        POSTER_MAP.put("Kantara", "images/kantara.png");
    }

    /**
     * Loads a scaled ImageIcon for the specified movie title.
     */
    public static ImageIcon getMoviePoster(String movieTitle, int width, int height) {
        String resourcePath = null;
        if (movieTitle != null) {
            for (Map.Entry<String, String> entry : POSTER_MAP.entrySet()) {
                if (movieTitle.toLowerCase().contains(entry.getKey().toLowerCase()) ||
                    entry.getKey().toLowerCase().contains(movieTitle.toLowerCase())) {
                    resourcePath = entry.getValue();
                    break;
                }
            }
        }

        if (resourcePath != null) {
            URL imgUrl = ImageLoader.class.getClassLoader().getResource(resourcePath);
            if (imgUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imgUrl);
                Image scaledImg = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
        }

        // Return a clean fallback placeholder icon if file not found
        return createFallbackPoster(movieTitle != null ? movieTitle : "Movie", width, height);
    }

    private static ImageIcon createFallbackPoster(String title, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(30, 41, 59));
        g2.fillRect(0, 0, width, height);

        g2.setColor(new Color(79, 70, 229));
        g2.drawRoundRect(8, 8, width - 16, height - 16, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("🎬 " + (title.length() > 15 ? title.substring(0, 12) + "..." : title), 16, height / 2);

        g2.dispose();
        return new ImageIcon(img);
    }
}
