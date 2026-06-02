package com.mycompany.concertbookingsystem;

import com.mycompany.concertbookingsystem.dao.BookingDAO;
import com.mycompany.concertbookingsystem.dao.ConcertDAO;
import com.mycompany.concertbookingsystem.dao.CustomerDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageBookingsFrame extends JFrame {
    private final DatabaseManager dbManager = new DatabaseManager();
    private final ConcertDAO concertDAO = new ConcertDAO(dbManager);
    private final CustomerDAO customerDAO = new CustomerDAO(dbManager);
    private final BookingDAO bookingDAO = new BookingDAO(dbManager);

    private final DefaultTableModel bookingsModel = new DefaultTableModel();
    private final JTable bookingsTable = new JTable(bookingsModel);
    private final JComboBox<String> customerCombo = new JComboBox<>();
    private final JComboBox<String> concertCombo = new JComboBox<>();
    private final JComboBox<String> seatTypeCombo = new JComboBox<>(new String[]{"BASIC", "VIP"});
    private final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
    private final JLabel priceLabel = new JLabel("Price: N/A");

    private List<Map<String, Object>> availableConcerts = new ArrayList<>();
    private List<Map<String, Object>> availableCustomers = new ArrayList<>();

    public ManageBookingsFrame() {
        setTitle("Manage Bookings");
        setSize(980, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JLabel header = new JLabel("Manage Bookings", SwingConstants.LEFT);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        bookingsModel.setColumnIdentifiers(new Object[]{"ID", "Customer", "Event", "Seat Type", "Quantity", "Total Price", "Status"});
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(bookingsTable);
        tableScroll.setPreferredSize(new Dimension(0, 320));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Create Booking"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, c, 0, "Customer:", customerCombo);
        addFormRow(formPanel, c, 1, "Concert:", concertCombo);
        addFormRow(formPanel, c, 2, "Seat Type:", seatTypeCombo);
        addFormRow(formPanel, c, 3, "Quantity:", quantitySpinner);
        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(priceLabel, c);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createBtn = new JButton("Book");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");
        createBtn.addActionListener(e -> createBooking());
        refreshBtn.addActionListener(e -> loadData());
        backBtn.addActionListener(e -> dispose());
        actionPanel.add(refreshBtn);
        actionPanel.add(createBtn);
        actionPanel.add(backBtn);

        JPanel rightPanel = new JPanel(new BorderLayout(10,10));
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(360, 0));

        add(tableScroll, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        concertCombo.addActionListener(e -> refreshPriceLabel());
        quantitySpinner.addChangeListener(e -> refreshPriceLabel());
        seatTypeCombo.addActionListener(e -> refreshPriceLabel());

        loadData();
    }

    private void addFormRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row; c.weightx = 0.0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.weightx = 1.0;
        field.setPreferredSize(new Dimension(220, 28));
        panel.add(field, c);
    }

    private void loadData() {
        loadCustomerOptions();
        loadConcertOptions();
        loadBookings();
    }

    private void loadCustomerOptions() {
        availableCustomers = customerDAO.getAllCustomers();
        customerCombo.removeAllItems();
        for (Map<String, Object> customer : availableCustomers) {
            customerCombo.addItem(String.valueOf(customer.getOrDefault("username", "")));
        }
    }

    private void loadConcertOptions() {
        availableConcerts = concertDAO.getAllConcerts();
        concertCombo.removeAllItems();
        for (Map<String, Object> concert : availableConcerts) {
            concertCombo.addItem(String.valueOf(concert.getOrDefault("title", "Untitled")));
        }
        refreshPriceLabel();
    }

    private void loadBookings() {
        bookingsModel.setRowCount(0);
        List<Map<String, Object>> rows = bookingDAO.getAllBookings();
        for (Map<String, Object> row : rows) {
            bookingsModel.addRow(new Object[]{row.get("id"), row.get("customer_name"), row.get("concert_name"), row.get("seat_type"), row.get("quantity"), row.get("ticket_price"), row.get("status")});
        }
    }

    private void refreshPriceLabel() {
        String concertTitle = (String) concertCombo.getSelectedItem();
        if (concertTitle == null || concertTitle.isEmpty()) {
            priceLabel.setText("Price: N/A");
            return;
        }
        Map<String, Object> selectedConcert = availableConcerts.stream()
                .filter(c -> concertTitle.equals(String.valueOf(c.getOrDefault("title", ""))))
                .findFirst().orElse(null);
        if (selectedConcert == null) {
            priceLabel.setText("Price: N/A");
            return;
        }
        double basePrice = ((Number) selectedConcert.getOrDefault("price", 0.0)).doubleValue();
        int qty = (Integer) quantitySpinner.getValue();
        double unitPrice = basePrice;
        if ("VIP".equals(seatTypeCombo.getSelectedItem())) {
            unitPrice += 50.0;
        }
        double total = unitPrice * qty;
        priceLabel.setText(String.format("Price per seat: ₱%.2f, Total: ₱%.2f", unitPrice, total));
    }

    private void createBooking() {
        String customer = (String) customerCombo.getSelectedItem();
        String concertTitle = (String) concertCombo.getSelectedItem();
        String seatType = (String) seatTypeCombo.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();

        if (customer == null || customer.isEmpty() || concertTitle == null || concertTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a customer and concert.");
            return;
        }
        Map<String, Object> selectedConcert = availableConcerts.stream()
                .filter(c -> concertTitle.equals(String.valueOf(c.getOrDefault("title", ""))))
                .findFirst().orElse(null);
        if (selectedConcert == null) {
            JOptionPane.showMessageDialog(this, "Selected concert not found.");
            return;
        }
        int concertId = ((Number) selectedConcert.getOrDefault("id", 0)).intValue();
        double basePrice = ((Number) selectedConcert.getOrDefault("price", 0.0)).doubleValue();
        double unitPrice = basePrice;
        if ("VIP".equalsIgnoreCase(seatType)) {
            unitPrice += 50.0;
        }
        boolean ok = bookingDAO.createBooking(customer, concertId, concertTitle, seatType, quantity, unitPrice);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Booking created successfully.");
            loadBookings();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create booking. Check seat availability.");
        }
    }
}
