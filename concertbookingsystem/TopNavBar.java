package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;

public class TopNavBar extends JPanel {
    private JLabel titleLabel;
    private final AuthManager authManager;

    public TopNavBar(MainDashboardFrame parent, String title, Color primaryColor, Color secondaryColor) {
        authManager = new AuthManager();
        setLayout(new BorderLayout());
        setBackground(secondaryColor);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 226, 242), 1),
                BorderFactory.createEmptyBorder(14, 24, 14, 24)));
        setPreferredSize(new Dimension(0, 76));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(260, 48));

        JButton brandLogo = new JButton("TICKS-EZ");
        brandLogo.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 24));
        brandLogo.setForeground(primaryColor);
        brandLogo.setContentAreaFilled(false);
        brandLogo.setBorderPainted(false);
        brandLogo.setFocusPainted(false);
        brandLogo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        brandLogo.addActionListener(e -> parent.showHomeCard());
        leftPanel.add(brandLogo);
        add(leftPanel, BorderLayout.WEST);

        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setForeground(primaryColor);
        add(titleLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(260, 48));

        JButton homeBtn = new JButton("Home");
        styleNavButton(homeBtn, primaryColor, false);
        homeBtn.addActionListener(e -> parent.showHomeCard());
        rightPanel.add(homeBtn);

        JButton logoutBtn = new JButton("Log Out");
        styleNavButton(logoutBtn, primaryColor, true);
        logoutBtn.addActionListener(e -> authManager.logout(parent));
        rightPanel.add(logoutBtn);
        add(rightPanel, BorderLayout.EAST);
    }

    public void setTitleText(String title) {
        titleLabel.setText(title);
    }

    private void styleNavButton(JButton button, Color primaryColor, boolean filled) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryColor, 1, true),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        button.setOpaque(true);
        if (filled) {
            button.setBackground(primaryColor);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(primaryColor);
        }
    }
}
