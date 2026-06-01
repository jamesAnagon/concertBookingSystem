package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;

public class TopNavBar extends JPanel {
    private JLabel titleLabel;

    public TopNavBar(MainDashboardFrame parent, String title, Color primaryColor, Color secondaryColor) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JButton brandLogo = new JButton("🎵 Bookist");
        brandLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        brandLogo.setForeground(primaryColor);
        brandLogo.setContentAreaFilled(false);
        brandLogo.setBorderPainted(false);
        brandLogo.setFocusPainted(false);
        brandLogo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        brandLogo.addActionListener(e -> {
            parent.showHomeCard();
        });
        add(brandLogo, BorderLayout.WEST);

        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(primaryColor);
        add(titleLabel, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setForeground(primaryColor);
        logoutBtn.addActionListener(e -> {
            HomePage.isLoggedIn = false;
            HomePage.currentUsername = "";
            parent.dispose();
            new LoginFrame().setVisible(true);
        });
        add(logoutBtn, BorderLayout.EAST);
    }

    public void setTitleText(String title) {
        titleLabel.setText(title);
    }
}
