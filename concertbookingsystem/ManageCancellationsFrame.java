package com.mycompany.concertbookingsystem;

import com.mycompany.concertbookingsystem.dao.BookingDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ManageCancellationsFrame extends JFrame {
    private final DatabaseManager dbManager = new DatabaseManager();
    private final BookingDAO bookingDAO = new BookingDAO(dbManager);

    private final JTextField bookingIdField = new JTextField();
    private final JTextArea bookingDetailsArea = new JTextArea();
    private final JButton cancelBtn = new JButton("Cancel Booking");

    public ManageCancellationsFrame() {
        setTitle("Manage Cancellations");
        setSize(700, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Manage Cancellations", SwingConstants.LEFT);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0.0;
        top.add(new JLabel("Booking ID:"), c);
        c.gridx = 1; c.weightx = 1.0;
        bookingIdField.setPreferredSize(new Dimension(220, 28));
        top.add(bookingIdField, c);
        c.gridx = 2; c.weightx = 0.0;
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchBooking());
        top.add(searchBtn, c);

        add(top, BorderLayout.NORTH);

        bookingDetailsArea.setEditable(false);
        bookingDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        bookingDetailsArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        add(new JScrollPane(bookingDetailsArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelBtn.setEnabled(false);
        cancelBtn.addActionListener(e -> cancelBooking());
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> dispose());
        bottom.add(cancelBtn);
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private void searchBooking() {
        String text = bookingIdField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a booking ID.");
            return;
        }
        try {
            int bookingId = Integer.parseInt(text);
            Map<String, Object> booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                bookingDetailsArea.setText("Booking not found.");
                cancelBtn.setEnabled(false);
                return;
            }
            String status = String.valueOf(booking.getOrDefault("status", ""));
            bookingDetailsArea.setText(String.format("Booking ID: %s\nCustomer: %s\nEvent: %s\nSeat Type: %s\nQuantity: %s\nPrice: %s\nStatus: %s\n",
                    booking.get("id"), booking.get("customer_name"), booking.get("concert_name"), booking.get("seat_type"), booking.get("quantity"), booking.get("ticket_price"), status));
            cancelBtn.setEnabled(!"CANCELLED".equalsIgnoreCase(status));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Booking ID must be a number.");
        }
    }

    private void cancelBooking() {
        String text = bookingIdField.getText().trim();
        if (text.isEmpty()) {
            return;
        }
        int bookingId = Integer.parseInt(text);
        int answer = JOptionPane.showConfirmDialog(this, "Cancel booking ID " + bookingId + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        boolean ok = bookingDAO.cancelBooking(bookingId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Booking cancelled and seat restored.");
            searchBooking();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to cancel booking. It may already be cancelled.");
        }
    }
}
