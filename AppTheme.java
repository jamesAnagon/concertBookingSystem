package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;

public final class AppTheme {
    public static final Color PRIMARY_BLUE = Color.decode("#1800ad");
    public static final Color WHITE = Color.WHITE;
    public static final Color SOFT_BACKGROUND = new Color(248, 248, 252);
    public static final Color PANEL_BACKGROUND = new Color(245, 245, 251);
    public static final Color BORDER_COLOR = new Color(226, 226, 242);
    public static final Color TEXT_BLUE = new Color(20, 20, 140);

    private AppTheme() {
    }

    public static Font boldSans(int size) {
        return new Font("SansSerif", Font.BOLD, size);
    }

    public static Font plainSans(int size) {
        return new Font("SansSerif", Font.PLAIN, size);
    }

    public static Font italicSerif(int size) {
        return new Font("Serif", Font.BOLD | Font.ITALIC, size);
    }

    public static void stylePrimaryButton(JButton button) {
        button.setBackground(PRIMARY_BLUE);
        button.setForeground(WHITE);
        button.setFont(boldSans(15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    public static void styleOutlinedButton(JButton button) {
        button.setBackground(WHITE);
        button.setForeground(PRIMARY_BLUE);
        button.setFont(boldSans(13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE, 1, true),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
    }
}
