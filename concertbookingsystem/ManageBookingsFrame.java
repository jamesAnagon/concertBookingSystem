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
    
    // Edit form components
    private final JTextField idField = new JTextField();
    private final JTextField customerNameField = new JTextField();
    private final JComboBox<String> seatTypeCombo = new JComboBox<>(new String[]{"BASIC", "VIP"});
    private final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

    private int selectedBookingId = -1;

    public ManageBookingsFrame() {
        setTitle("Manage Bookings");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("Manage Bookings - View, Update & Delete", SwingConstants.LEFT);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        // Table setup
        bookingsModel.setColumnIdentifiers(new Object[]{"ID", "Customer", "Event", "Seat Type", "Quantity", "Total Price", "Status"});
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.getSelectionModel().addListSelectionListener(e -> onTableSelect());
        JScrollPane tableScroll = new JScrollPane(bookingsTable);
        tableScroll.setPreferredSize(new Dimension(0, 340));

        // Form panel for editing
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Edit Booking"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        idField.setEditable(false);

        addFormRow(formPanel, c, 0, "ID:", idField);
        addFormRow(formPanel, c, 1, "Customer Name:", customerNameField);
        addFormRow(formPanel, c, 2, "Seat Type:", seatTypeCombo);
        addFormRow(formPanel, c, 3, "Quantity:", quantitySpinner);

        // Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton refreshBtn = new JButton("Refresh");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        refreshBtn.addActionListener(e -> loadData());
        updateBtn.addActionListener(e -> updateBooking());
        deleteBtn.addActionListener(e -> deleteBooking());
        backBtn.addActionListener(e -> dispose());

        actionPanel.add(refreshBtn);
        actionPanel.add(updateBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(backBtn);

        JPanel rightPanel = new JPanel(new BorderLayout(10,10));
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(380, 0));

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        centerPanel.add(rightPanel, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);

        loadData();
    }

    private void addFormRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row; c.weightx = 0.0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.weightx = 1.0;
        field.setPreferredSize(new Dimension(240, 28));
        panel.add(field, c);
    }

    private void loadData() {
        loadBookings();
    }

    private void loadBookings() {
        bookingsModel.setRowCount(0);
        List<Map<String, Object>> rows = bookingDAO.getAllBookings();
        for (Map<String, Object> row : rows) {
            bookingsModel.addRow(new Object[]{
                    row.get("id"), 
                    row.get("customer_name"), 
                    row.get("concert_name"), 
                    row.get("seat_type"), 
                    row.get("quantity"), 
                    row.get("ticket_price"), 
                    row.get("status")
            });
        }
    }

    private void onTableSelect() {
        int sel = bookingsTable.getSelectedRow();
        if (sel < 0) {
            clearForm();
            return;
        }
        int modelRow = bookingsTable.convertRowIndexToModel(sel);
        selectedBookingId = (Integer) bookingsModel.getValueAt(modelRow, 0);
        idField.setText(String.valueOf(selectedBookingId));
        customerNameField.setText(String.valueOf(bookingsModel.getValueAt(modelRow, 1)));
        seatTypeCombo.setSelectedItem(String.valueOf(bookingsModel.getValueAt(modelRow, 3)));
        quantitySpinner.setValue((Integer) bookingsModel.getValueAt(modelRow, 4));
    }

    private void clearForm() {
        selectedBookingId = -1;
        idField.setText("");
        customerNameField.setText("");
        seatTypeCombo.setSelectedIndex(0);
        quantitySpinner.setValue(1);
    }

    /**
     * Update the selected booking using PreparedStatement
     */
    private void updateBooking() {
        if (selectedBookingId < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to update.");
            return;
        }
        
        String customerName = customerNameField.getText().trim();
        String seatType = (String) seatTypeCombo.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();
        
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Customer name cannot be empty.");
            return;
        }
        
        boolean success = bookingDAO.updateBooking(selectedBookingId, customerName, seatType, quantity);
        if (success) {
            JOptionPane.showMessageDialog(this, "Booking updated successfully.");
            loadBookings();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update booking.");
        }
    }

    /**
     * Delete the selected booking using PreparedStatement
     */
    private void deleteBooking() {
        if (selectedBookingId < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to delete.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete booking ID " + selectedBookingId + "?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        boolean success = bookingDAO.deleteBooking(selectedBookingId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Booking deleted successfully.");
            loadBookings();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete booking.");
        }
    }
}
