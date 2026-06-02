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

        JPanel grid = new JPanel(new GridLayout(2, 4, 12, 12));

        grid.add(makeNavButton("Manage Concerts", e -> openManageConcerts()));
        grid.add(makeNavButton("Manage Customers", e -> openManageCustomers()));
        grid.add(makeNavButton("Manage Bookings", e -> openManageBookings()));
        grid.add(makeNavButton("Manage Cancellations", e -> openManageCancellations()));
        grid.add(makeNavButton("Billing Summary", e -> openBillingSummary()));
        grid.add(makeNavButton("Booking History", e -> openBookingHistory()));
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

    private void openSubPanel(String title) {
        SubPanelFrame panel = new SubPanelFrame(this, title);
        panel.setVisible(true);
        this.setVisible(false);
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

    private void openManageCancellations() {
        ManageCancellationsFrame f = new ManageCancellationsFrame();
        f.setVisible(true);
        this.setVisible(false);
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                AdminDashboardFrame.this.setVisible(true);
            }
        });
    }

    private void openBillingSummary() {
        BillingSummaryFrame f = new BillingSummaryFrame();
        f.setVisible(true);
        this.setVisible(false);
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                AdminDashboardFrame.this.setVisible(true);
            }
        });
    }

    private void openBookingHistory() {
        BookingHistoryFrame f = new BookingHistoryFrame();
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

    // Simple generic sub-panel frame that shows a JTable placeholder and a Back button
    private static class SubPanelFrame extends JFrame {
        SubPanelFrame(AdminDashboardFrame parent, String title) {
            setTitle(title);
            setSize(800, 520);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel root = new JPanel(new BorderLayout(8,8));
            root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

            JLabel header = new JLabel(title, SwingConstants.LEFT);
            header.setFont(new Font("SansSerif", Font.BOLD, 20));
            root.add(header, BorderLayout.NORTH);

            // Placeholder table - will be replaced by real JTable in full implementation
            String[] cols = {"ID", "Column1", "Column2", "Status"};
            Object[][] data = {{"-", "(data)", "(data)", "-"}};
            JTable table = new JTable(data, cols);
            JScrollPane sp = new JScrollPane(table);
            root.add(sp, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton back = new JButton("Back");
            back.addActionListener(e -> {
                this.dispose();
                parent.setVisible(true);
            });
            bottom.add(back);

            // Add section-specific action placeholders
            JButton action = new JButton("Action");
            action.addActionListener(e -> JOptionPane.showMessageDialog(this, "Placeholder action for " + title));
            bottom.add(action);

            root.add(bottom, BorderLayout.SOUTH);
            add(root);
        }
    }
}
