package com.movieticket.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Generates local movie poster artwork images into src/main/resources/images/
 */
public class PosterGenerator {

    public static void generatePosters() {
        File dir = new File("src/main/resources/images");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Object[][] movies = {
            {"inception.png", "INCEPTION", "SCI-FI / ACTION", "PG-13", new Color(15, 23, 42), new Color(59, 130, 246)},
            {"interstellar.png", "INTERSTELLAR", "SCI-FI / ADVENTURE", "PG-13", new Color(15, 15, 35), new Color(139, 92, 246)},
            {"dark_knight.png", "THE DARK KNIGHT", "ACTION / CRIME", "PG-13", new Color(20, 20, 20), new Color(234, 179, 8)},
            {"avatar.png", "AVATAR", "WAY OF WATER", "PG-13", new Color(6, 78, 114), new Color(6, 182, 212)},
            {"pushpa2.png", "PUSHPA 2", "THE RULE", "UA", new Color(120, 20, 20), new Color(249, 115, 22)},
            {"kantara.png", "KANTARA", "ACTION / THRILLER", "UA", new Color(45, 20, 10), new Color(234, 88, 12)}
        };

        int width = 300;
        int height = 420;

        for (Object[] m : movies) {
            String filename = (String) m[0];
            String title = (String) m[1];
            String subtitle = (String) m[2];
            String rating = (String) m[3];
            Color bgDark = (Color) m[4];
            Color bgAccent = (Color) m[5];

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Gradient Background
            GradientPaint gp = new GradientPaint(0, 0, bgDark, 0, height, bgDark.darker());
            g2.setPaint(gp);
            g2.fillRect(0, 0, width, height);

            // Inner Accent Border Frame
            g2.setColor(bgAccent);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(12, 12, width - 24, height - 24, 12, 12);
            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawRoundRect(18, 18, width - 36, height - 36, 8, 8);

            // Emblem / Cinema Star Box
            int cx = width / 2;
            int cy = 130;
            g2.setColor(bgDark.darker());
            g2.fillOval(cx - 45, cy - 45, 90, 90);
            g2.setColor(bgAccent);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(cx - 45, cy - 45, 90, 90);

            // Star Emblem
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(bgAccent);
            FontMetrics fm = g2.getFontMetrics();
            int starWidth = fm.stringWidth("🎬");
            g2.drawString("🎬", cx - starWidth / 2, cy + 12);

            // Rating Badge (Top Right)
            g2.setColor(bgAccent);
            g2.fillRoundRect(width - 75, 25, 50, 24, 6, 6);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString(rating, width - 65, 42);

            // Movie Info Card Area (Bottom Overlay)
            g2.setColor(new Color(15, 23, 42, 220));
            g2.fillRoundRect(22, 230, width - 44, 160, 10, 10);
            g2.setColor(bgAccent);
            g2.drawRoundRect(22, 230, width - 44, 160, 10, 10);

            // Title Text
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.drawString(title, 32, 265);

            // Subtitle / Genre Text
            g2.setColor(bgAccent);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString(subtitle, 32, 295);

            // Cinema Tag
            g2.setColor(new Color(203, 213, 225));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("NOW SHOWING IN CINEMAS", 32, 335);

            // Price Badge Pill
            g2.setColor(new Color(34, 197, 94));
            g2.fillRoundRect(32, 350, 100, 24, 6, 6);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("TICKETS READY", 40, 366);

            g2.dispose();

            File outFile = new File(dir, filename);
            try {
                ImageIO.write(img, "png", outFile);
                System.out.println("Generated: " + outFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        generatePosters();
    }
}
