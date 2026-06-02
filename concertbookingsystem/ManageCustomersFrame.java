package com.mycompany.concertbookingsystem;

import com.mycompany.concertbookingsystem.dao.CustomerDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ManageCustomersFrame extends JFrame {
    private final DatabaseManager dbManager = new DatabaseManager();
    private final CustomerDAO customerDAO = new CustomerDAO(dbManager);

    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable customersTable = new JTable(tableModel);

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();

    public ManageCustomersFrame() {
        setTitle("Manage Customers");
        setSize(840, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Manage Customers", SwingConstants.LEFT);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(header, BorderLayout.NORTH);

        tableModel.setColumnIdentifiers(new Object[]{"ID", "Username", "Total Bookings"});
        customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(customersTable);
        tableScroll.setPreferredSize(new Dimension(0, 280));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Register New Customer"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, c, 0, "Username:", usernameField);
        addFormRow(formPanel, c, 1, "Password:", passwordField);
        addFormRow(formPanel, c, 2, "Confirm Password:", confirmPasswordField);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton registerBtn = new JButton("Register");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");
        registerBtn.addActionListener(e -> onRegister());
        refreshBtn.addActionListener(e -> loadCustomers());
        backBtn.addActionListener(e -> dispose());
        buttonRow.add(refreshBtn);
        buttonRow.add(registerBtn);
        buttonRow.add(backBtn);

        JPanel centerPanel = new JPanel(new BorderLayout(12, 12));
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        centerPanel.add(formPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonRow, BorderLayout.SOUTH);

        loadCustomers();
    }

    private void addFormRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row; c.weightx = 0.0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.weightx = 1.0;
        field.setPreferredSize(new Dimension(240, 26));
        panel.add(field, c);
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> rows = customerDAO.getAllCustomers();
        for (Map<String, Object> row : rows) {
            tableModel.addRow(new Object[]{row.get("id"), row.get("username"), row.get("total_bookings")});
        }
    }

    private void onRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }
        if (customerDAO.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
            return;
        }
        boolean ok = customerDAO.registerCustomer(username, password);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Customer registered successfully.");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            loadCustomers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register customer.");
        }
    }
}
