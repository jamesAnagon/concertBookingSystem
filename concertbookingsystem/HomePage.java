package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HomePage extends JFrame {
    
    // Global session tracker to see if a guest user has logged in yet
    public static boolean isLoggedIn = false;
    public static String currentUsername = "";
    // Brand Colors
    private final Color PRIMARY_COLOR = Color.decode("#1800ad");
    private final Color SECONDARY_COLOR = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color TEXT_COLOR = new Color(20, 20, 140);
    private final Color MUTED_TEXT = Color.decode("#C7C2EB");
    
    public HomePage() {
        setTitle("Bookist");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start Fullscreen
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // App-wide unified exit validation wrapper
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmQuit();
            }
        });

        // 1. Root visual structure panel utilizing a vibrant modern blue backdrop
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(24, 119, 242)); // Modern Digital Blue
        
        // --- SECTION A: TOP HEADER NAVIGATION BAR ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false); // Transparent so the root blue shines through
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Brand Logo placement on the left
        JLabel logoLabel = new JLabel("🎵 Bookist");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Navigation Action controls grouped closely on the right
        JPanel navRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navRightPanel.setOpaque(false);

        JButton signInBtn = new JButton("Sign In");
        JButton signUpBtn = new JButton("Sign Up / Register");
        styleHeaderButton(signInBtn);
        styleHeaderButton(signUpBtn);

        navRightPanel.add(signInBtn);
        navRightPanel.add(signUpBtn);
        headerPanel.add(navRightPanel, BorderLayout.EAST);
        
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // --- SECTION B: HERO INNER CALL-TO-ACTION CENTER ---
        // A Boxlayout stack to neatly keep layout fields centered regardless of monitor resolution scaling
        JPanel heroCenterWrapper = new JPanel();
        heroCenterWrapper.setLayout(new BoxLayout(heroCenterWrapper, BoxLayout.Y_AXIS));
        heroCenterWrapper.setOpaque(false);
        
        // Vertical spacer formatting strings
        heroCenterWrapper.add(Box.createVerticalGlue());

        JLabel mainHeading = new JLabel("START YOUR CONCERT ADVENTURE");
        mainHeading.setFont(new Font("SansSerif", Font.BOLD, 36));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        heroCenterWrapper.add(mainHeading);

        heroCenterWrapper.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subHeading = new JLabel("Connect with live experiences anytime, anywhere!");
        subHeading.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subHeading.setForeground(new Color(220, 235, 255));
        subHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        heroCenterWrapper.add(subHeading);

        heroCenterWrapper.add(Box.createRigidArea(new Dimension(0, 40)));

        // Core CTA Hero Action Button
        JButton bookNowBtn = new JButton("BOOK NOW 🎟️");
        bookNowBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        bookNowBtn.setForeground(Color.WHITE);
        bookNowBtn.setBackground(new Color(254, 44, 85)); // Vibrant Crimson/Coral Accent
        bookNowBtn.setFocusPainted(false);
        bookNowBtn.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        bookNowBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookNowBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        heroCenterWrapper.add(bookNowBtn);

        heroCenterWrapper.add(Box.createVerticalGlue());
        rootPanel.add(heroCenterWrapper, BorderLayout.CENTER);

        // --- SECTION C: FOOTER GATEWAY ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton adminLoginBtn = new JButton("🔒 System Admin Portal Access");
        adminLoginBtn.setFont(new Font("SansSerif", Font.ITALIC, 12));
        adminLoginBtn.setForeground(new Color(200, 220, 255));
        adminLoginBtn.setContentAreaFilled(false);
        adminLoginBtn.setBorderPainted(false);
        adminLoginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footerPanel.add(adminLoginBtn);

        rootPanel.add(footerPanel, BorderLayout.SOUTH);
        add(rootPanel);

        // --- SECTION D: CONDITIONAL ROUTING CONTROLS ---
        
        // BOOK NOW BUTTON INTERCEPT LOGIC
        bookNowBtn.addActionListener(e -> {
            if (!isLoggedIn) {
                JOptionPane.showMessageDialog(this, "Authentication Required: Redirecting you to the Login Screen.");
                this.dispose();
                new LoginFrame().setVisible(true); 
            } else {
                this.dispose();
                new MainDashboardFrame().setVisible(true);
            }
        });

        // Redirect links for the header navigation elements
        signInBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        signUpBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true); // Uses the integrated registration block on your LoginFrame
        });

        adminLoginBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Admin Gateway initialized. (Default root verification protocol active).");
            this.dispose();
            new MainDashboardFrame().setVisible(true);
        });
    }

    // Helper utility styling header buttons
    private void styleHeaderButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void confirmQuit() {
        int reply = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to quit?", "Exit Application", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
