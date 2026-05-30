package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainDashboardFrame extends JFrame {
    private DatabaseManager dbManager;

    public MainDashboardFrame() {
        dbManager = new DatabaseManager();
        
        setTitle("Concert Booking System Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmQuit();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("🎟️ Concert Booking Management Panel", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Buttons for interactive CRUD actions
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton createBtn = new JButton("Book a New Ticket");
        JButton viewBtn = new JButton("View All Bookings");
        JButton updateBtn = new JButton("Modify a Booking Name");
        JButton deleteBtn = new JButton("Cancel a Booking");

        btnPanel.add(createBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        mainPanel.add(btnPanel, BorderLayout.CENTER);

        JButton logOutBtn = new JButton("Log Out");
        mainPanel.add(logOutBtn, BorderLayout.SOUTH);

        add(mainPanel);

        // Action Logic using your existing OOP structures and polymorphism
        createBtn.addActionListener(e -> {
            // Polymorphism example using a simple UI dialog box selection
            String[] strategies = {"Regular Ticket", "VIP Ticket"};
            int choice = JOptionPane.showOptionDialog(this, "Select Ticket Class:", "New Booking",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, strategies, strategies[0]);
            
            String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
            if (name == null || name.trim().isEmpty()) return;
            
            if (choice == 0) {
                Ticket t = new RegularTicket(name, "Coldplay World Tour", 120.0);
                dbManager.createBooking(t);
            } else if (choice == 1) {
                Ticket t = new VipTicket(name, "Taylor Swift Eras", 300.0, "Front Row & Lounge Access");
                dbManager.createBooking(t);
            }
            JOptionPane.showMessageDialog(this, "Booking successfully added!");
        });

        viewBtn.addActionListener(e -> {
            // This prints to console output panel right now
            dbManager.readBookings();
            JOptionPane.showMessageDialog(this, "Check your NetBeans terminal output window to see current records!");
        });

        updateBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter Booking ID to update:");
            if (idStr == null) return;
            String newName = JOptionPane.showInputDialog(this, "Enter New Customer Name:");
            if (newName == null) return;
            
            try {
                int id = Integer.parseInt(idStr);
                dbManager.updateCustomerName(id, newName);
                JOptionPane.showMessageDialog(this, "Update command processed.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter Booking ID to delete:");
            if (idStr == null) return;
            try {
                int id = Integer.parseInt(idStr);
                dbManager.deleteBooking(id);
                JOptionPane.showMessageDialog(this, "Deletion command processed.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logOutBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
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
