package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminDashboardFrame extends JFrame {
    private final Color PRIMARY_COLOR = Color.decode("#1800ad");
    private final Color SECONDARY_COLOR = Color.WHITE;

    public AdminDashboardFrame() {
        setTitle("Admin Dashboard - Concert Booking System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmLogout();
            }
        });

        JPanel root = new JPanel(new BorderLayout(12,12));
        root.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));

        JLabel header = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(PRIMARY_COLOR);
        root.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 4, 12, 12));

        grid.add(makeNavButton("Manage Concerts", e -> openManageConcerts()));
        grid.add(makeNavButton("Manage Customers", e -> openManageCustomers()));
        grid.add(makeNavButton("Manage Bookings", e -> openManageBookings()));
        grid.add(makeNavButton("Logout", e -> handleLogout()));

        root.add(grid, BorderLayout.CENTER);

        add(root);
    }

    private JButton makeNavButton(String text, java.awt.event.ActionListener action) {
        JButton b = new JButton(text);
        b.setBackground(PRIMARY_COLOR);
        b.setForeground(SECONDARY_COLOR);
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.addActionListener(action);
        return b;
    }

    private void openManageConcerts() {
        ManageConcertsFrame f = new ManageConcertsFrame();
        f.setVisible(true);
        this.setVisible(false);
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                AdminDashboardFrame.this.setVisible(true);
            }
        });
    }

    private void openManageCustomers() {
        ManageCustomersFrame f = new ManageCustomersFrame();
        f.setVisible(true);
        this.setVisible(false);
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                AdminDashboardFrame.this.setVisible(true);
            }
        });
    }

    private void openManageBookings() {
        ManageBookingsFrame f = new ManageBookingsFrame();
        f.setVisible(true);
        this.setVisible(false);
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                AdminDashboardFrame.this.setVisible(true);
            }
        });
    }

    private void handleLogout() {
        int reply = JOptionPane.showConfirmDialog(this, "Logout and return to login?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            UserSession.logout();
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }

    private void confirmLogout() {
        int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
