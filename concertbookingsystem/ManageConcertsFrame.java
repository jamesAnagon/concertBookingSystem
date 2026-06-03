package com.mycompany.concertbookingsystem;

import com.mycompany.concertbookingsystem.dao.ConcertDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Locale;

public class ManageConcertsFrame extends JFrame {
    private final DatabaseManager dbManager = new DatabaseManager();
    private final ConcertDAO concertDAO = new ConcertDAO(dbManager);
    
    // Date formatters
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMMM, dd, yyyy", Locale.ENGLISH);

    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable concertsTable = new JTable(tableModel);

    private final JTextField idField = new JTextField();
    private final JTextField concertNameField = new JTextField();
    private final JTextField dateField = new JTextField(); // Date picker input
    private final JTextField regularPriceField = new JTextField();
    private final JTextField vipPriceField = new JTextField();

    public ManageConcertsFrame() {
        setTitle("Manage Concerts");
        setSize(920, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        JPanel top = new JPanel(new BorderLayout());
        JLabel header = new JLabel("Manage Concerts", SwingConstants.LEFT);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        top.add(header, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        // Table setup
        tableModel.setColumnIdentifiers(new Object[]{"id","concert_name","concert_date","regular_price","vip_price"});
        concertsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        concertsTable.getSelectionModel().addListSelectionListener(e -> onTableSelect());
        JScrollPane sp = new JScrollPane(concertsTable);
        sp.setPreferredSize(new Dimension(0,300));

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addFormRow(form, c, row++, "ID (auto):", idField, false);
        addFormRow(form, c, row++, "Concert Name:", concertNameField, true);
        addFormRow(form, c, row++, "Date (YYYY-MM-DD):", dateField, true);
        addFormRow(form, c, row++, "Regular Price:", regularPriceField, true);
        addFormRow(form, c, row++, "VIP Price:", vipPriceField, true);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> onAdd());
        updateBtn.addActionListener(e -> onUpdate());
        deleteBtn.addActionListener(e -> onDelete());
        refreshBtn.addActionListener(e -> loadConcerts());
        backBtn.addActionListener(e -> { this.dispose(); });

        buttons.add(refreshBtn);
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(backBtn);

        JPanel center = new JPanel(new BorderLayout(8,8));
        center.add(sp, BorderLayout.CENTER);
        center.add(form, BorderLayout.EAST);
        add(center, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        idField.setEditable(false);
        loadConcerts();
    }

    private void addFormRow(JPanel panel, GridBagConstraints c, int row, String label, JTextField field, boolean enabled) {
        c.gridx = 0; c.gridy = row; c.weightx = 0.0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.weightx = 1.0;
        field.setEnabled(enabled);
        field.setPreferredSize(new Dimension(200, 26));
        panel.add(field, c);
    }

    private void loadConcerts() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> rows = concertDAO.getAllConcerts();
        for (Map<String, Object> r : rows) {
            String storedDate = String.valueOf(r.get("concert_date"));
            String displayDate = formatDateForDisplay(storedDate);
            tableModel.addRow(new Object[]{
                    r.get("id"), r.get("concert_name"), displayDate, r.get("regular_price"), r.get("vip_price")
            });
        }
    }

    /**
     * Converts stored YYYY-MM-DD format to MonthName, Day, Year for display
     */
    private String formatDateForDisplay(String storedDate) {
        try {
            LocalDate date = LocalDate.parse(storedDate, STORAGE_FORMAT);
            return date.format(DISPLAY_FORMAT);
        } catch (Exception e) {
            return storedDate; 
        }
    }

    private void onTableSelect() {
        int sel = concertsTable.getSelectedRow();
        if (sel < 0) {
            clearForm();
            return;
        }
        int modelRow = concertsTable.convertRowIndexToModel(sel);
        idField.setText(String.valueOf(tableModel.getValueAt(modelRow, 0)));
        concertNameField.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
        // Store the display date but we need to convert back to YYYY-MM-DD format for editing
        String displayDate = String.valueOf(tableModel.getValueAt(modelRow, 2));
        dateField.setText(convertDisplayDateToStorage(displayDate));
        regularPriceField.setText(String.valueOf(tableModel.getValueAt(modelRow, 3)));
        vipPriceField.setText(String.valueOf(tableModel.getValueAt(modelRow, 4)));
    }

    /**
     * Converts display format (MonthName, Day, Year) back to YYYY-MM-DD for editing
     */
    private String convertDisplayDateToStorage(String displayDate) {
        try {
            LocalDate date = LocalDate.parse(displayDate, DISPLAY_FORMAT);
            return date.format(STORAGE_FORMAT);
        } catch (Exception e) {
            return displayDate; // Return original if parsing fails
        }
    }

    private void clearForm() {
        idField.setText("");
        concertNameField.setText("");
        dateField.setText("");
        regularPriceField.setText("");
        vipPriceField.setText("");
    }

    private boolean validateForm(boolean requireId) {
        if (requireId && idField.getText().trim().isEmpty()) return false;
        if (concertNameField.getText().trim().isEmpty()) return false;
        if (dateField.getText().trim().isEmpty()) return false;
        try {
            LocalDate.parse(dateField.getText().trim(), STORAGE_FORMAT);
        } catch (DateTimeParseException ex) {
            return false;
        }
        try { Double.parseDouble(regularPriceField.getText().trim()); } catch (Exception ex) { return false; }
        try { Double.parseDouble(vipPriceField.getText().trim()); } catch (Exception ex) { return false; }
        return true;
    }

    private void onAdd() {
        if (!validateForm(false)) {
            JOptionPane.showMessageDialog(this, "Please fill all fields correctly (date format yyyy-MM-dd).");
            return;
        }
        try {
            java.sql.Date d = java.sql.Date.valueOf(LocalDate.parse(dateField.getText().trim(), STORAGE_FORMAT));
            String concertName = concertNameField.getText().trim();
            double regularPrice = Double.parseDouble(regularPriceField.getText().trim());
            double vipPrice = Double.parseDouble(vipPriceField.getText().trim());
            boolean ok = concertDAO.addConcert(concertName, d, regularPrice, vipPrice);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Concert added.");
                loadConcerts();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add concert.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onUpdate() {
        if (!validateForm(true)) {
            JOptionPane.showMessageDialog(this, "Please select a row and ensure all fields are valid.");
            return;
        }
        try {
            int id = Integer.parseInt(idField.getText().trim());
            java.sql.Date d = java.sql.Date.valueOf(LocalDate.parse(dateField.getText().trim(), STORAGE_FORMAT));
            String concertName = concertNameField.getText().trim();
            double regularPrice = Double.parseDouble(regularPriceField.getText().trim());
            double vipPrice = Double.parseDouble(vipPriceField.getText().trim());
            boolean ok = concertDAO.updateConcert(id, concertName, d, regularPrice, vipPrice);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Concert updated.");
                loadConcerts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update concert.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onDelete() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a concert to delete.");
            return;
        }
        int id = Integer.parseInt(idField.getText().trim());
        int r = JOptionPane.showConfirmDialog(this, "Delete concert ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        boolean ok = concertDAO.deleteConcert(id);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Concert deleted.");
            loadConcerts();
            clearForm();
        } else JOptionPane.showMessageDialog(this, "Failed to delete concert. It may be referenced by bookings.");
    }
}
