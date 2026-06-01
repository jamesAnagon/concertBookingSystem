package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeatSelectionFrame extends JFrame {
    private final MainDashboardFrame parent;
    private final DatabaseManager dbManager;
    private final String eventName;
    private final Color primaryColor;
    private final Color secondaryColor;

    private final JLabel eventLabel;
    private final Set<String> selectedSeats = new HashSet<>();
    private final Map<String, JToggleButton> seatButtons = new HashMap<>();
    private final Set<String> bookedByOthers = new HashSet<>();
    private final Set<String> bookedByCurrentUser = new HashSet<>();

    public SeatSelectionFrame(MainDashboardFrame parent, DatabaseManager dbManager, String eventName, Color primaryColor, Color secondaryColor) {
        this.parent = parent;
        this.dbManager = dbManager;
        this.eventName = eventName;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;

        setTitle("Seat Selection — " + eventName);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parent.setVisible(true);
                parent.toFront();
            }
        });

        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBackground(new Color(245, 245, 251));
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setContentPane(root);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor));
        JButton back = new JButton("← Back");
        back.setFont(new Font("SansSerif", Font.BOLD, 14));
        back.setForeground(primaryColor);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> dispose());
        topBar.add(back, BorderLayout.WEST);

        eventLabel = new JLabel("Seat Selection — " + eventName, SwingConstants.CENTER);
        eventLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        eventLabel.setForeground(primaryColor);
        topBar.add(eventLabel, BorderLayout.CENTER);

        JLabel help = new JLabel("Choose your seats below");
        help.setFont(new Font("SansSerif", Font.PLAIN, 14));
        help.setForeground(Color.DARK_GRAY);
        topBar.add(help, BorderLayout.EAST);

        root.add(topBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        centerPanel.add(buildSectionHeader("Regular Seats"));
        centerPanel.add(buildSeatBlock(3, 6, 1));
        centerPanel.add(buildSectionSeparator());
        centerPanel.add(buildSectionHeader("VIP Seats"));
        centerPanel.add(buildSeatBlock(2, 6, 4));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        centerWrapper.add(scroll, BorderLayout.CENTER);
        root.add(centerWrapper, BorderLayout.CENTER);

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 6));
        legend.setOpaque(false);
        legend.add(createLegendItem("X", "Already booked", new Color(220, 80, 80)));
        legend.add(createLegendItem("✔", "Booked by you", new Color(80, 180, 110)));
        legend.add(createLegendItem("💺", "Available seat", new Color(100, 130, 190)));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        JButton confirm = new JButton("Confirm Booking");
        confirm.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirm.setBackground(primaryColor);
        confirm.setForeground(secondaryColor);
        confirm.setFocusPainted(false);
        confirm.addActionListener(e -> doBooking());
        bottom.add(confirm);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(legend, BorderLayout.NORTH);
        southPanel.add(bottom, BorderLayout.SOUTH);
        root.add(southPanel, BorderLayout.SOUTH);

        loadBookingState();
        refreshAllSeats();
    }

    private JPanel buildSectionHeader(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(primaryColor);
        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private JComponent buildSectionSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(primaryColor);
        separator.setPreferredSize(new Dimension(0, 4));
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        wrapper.add(separator, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildSeatBlock(int rows, int cols, int startRow) {
        JPanel block = new JPanel(new GridLayout(rows, cols, 12, 12));
        block.setOpaque(true);
        block.setBackground(new Color(238, 238, 249));
        block.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        for (int r = 0; r < rows; r++) {
            for (int c = 1; c <= cols; c++) {
                int rowNumber = startRow + r;
                String id = "R" + rowNumber + "C" + c;
                JToggleButton seat = createSeatButton(id);
                seatButtons.put(id, seat);
                block.add(seat);
            }
        }
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(block, BorderLayout.CENTER);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryColor, 2),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        return wrapper;
    }

    private JToggleButton createSeatButton(String seatId) {
        JToggleButton seat = new JToggleButton(getSeatLabel(seatId));
        seat.setFont(new Font("SansSerif", Font.BOLD, 13));
        seat.setFocusPainted(false);
        seat.setOpaque(true);
        seat.setBackground(new Color(248, 248, 252));
        seat.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 2));
        seat.addActionListener(e -> {
            if (bookedByOthers.contains(seatId) || bookedByCurrentUser.contains(seatId)) {
                seat.setSelected(false);
                return;
            }
            if (seat.isSelected()) {
                selectedSeats.add(seatId);
            } else {
                selectedSeats.remove(seatId);
            }
            refreshSeatButton(seatId);
        });
        return seat;
    }

    private String getSeatLabel(String seatId) {
        return "<html><center>💺<br>" + seatId + "</center></html>";
    }

    private JPanel createLegendItem(String icon, String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panel.setOpaque(false);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        iconLabel.setForeground(color);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(iconLabel);
        panel.add(textLabel);
        return panel;
    }

    private void loadBookingState() {
        String currentUser = HomePage.currentUsername != null ? HomePage.currentUsername : "Guest";
        for (Map<String, Object> booking : dbManager.getBookingsForEvent(eventName)) {
            String concertName = (String) booking.get("concert_name");
            if (concertName == null) continue;
            int start = concertName.lastIndexOf("(");
            int end = concertName.lastIndexOf(")");
            if (start > 0 && end > start) {
                String seat = concertName.substring(start + 1, end);
                String customer = (String) booking.get("customer_name");
                if (currentUser.equals(customer)) {
                    bookedByCurrentUser.add(seat);
                } else {
                    bookedByOthers.add(seat);
                }
            }
        }
    }

    private void refreshAllSeats() {
        for (String seatId : seatButtons.keySet()) {
            refreshSeatButton(seatId);
        }
    }

    private void refreshSeatButton(String seatId) {
        JToggleButton seat = seatButtons.get(seatId);
        if (seat == null) return;

        if (bookedByOthers.contains(seatId)) {
            seat.setText("<html><center><span style='color:#B22222;font-size:18px;'>X</span><br>" + seatId + "</center></html>");
            seat.setEnabled(false);
            seat.setSelected(false);
            seat.setBackground(new Color(245, 220, 220));
            seat.setForeground(new Color(140, 30, 30));
            seat.setBorder(BorderFactory.createLineBorder(new Color(180, 40, 40), 2));
        } else if (bookedByCurrentUser.contains(seatId)) {
            seat.setText("<html><center><span style='color:#1F7A1F;font-size:18px;'>✔</span><br>" + seatId + "</center></html>");
            seat.setEnabled(false);
            seat.setSelected(true);
            seat.setBackground(new Color(210, 245, 220));
            seat.setForeground(new Color(25, 100, 40));
            seat.setBorder(BorderFactory.createLineBorder(new Color(90, 160, 100), 2));
        } else if (selectedSeats.contains(seatId)) {
            seat.setText("<html><center><span style='color:#114F8E;font-size:18px;'>✔</span><br>" + seatId + "</center></html>");
            seat.setEnabled(true);
            seat.setBackground(new Color(200, 235, 255));
            seat.setForeground(new Color(20, 50, 120));
            seat.setBorder(BorderFactory.createLineBorder(new Color(70, 120, 180), 2));
        } else {
            seat.setText(getSeatLabel(seatId));
            seat.setEnabled(true);
            seat.setSelected(false);
            seat.setBackground(Color.WHITE);
            seat.setForeground(Color.DARK_GRAY);
            seat.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        }
    }

    private void doBooking() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one seat.");
            return;
        }

        String user = HomePage.currentUsername != null ? HomePage.currentUsername : "Guest";
        List<String> bookedSeats = new ArrayList<>(selectedSeats);
        double totalPrice = 0;

        for (String seat : bookedSeats) {
            Ticket t;
            if (seat.startsWith("VIP")) {
                t = new VipTicket(user, eventName + " (" + seat + ")", 300.0, "VIP Lounge");
                totalPrice += 300.0;
            } else {
                t = new RegularTicket(user, eventName + " (" + seat + ")", 120.0);
                totalPrice += 120.0;
            }
            dbManager.createBooking(t);
        }

        dispose();

        BookingSummaryFrame summary = new BookingSummaryFrame(parent, user, eventName, bookedSeats, totalPrice);
        summary.setVisible(true);
    }
}
