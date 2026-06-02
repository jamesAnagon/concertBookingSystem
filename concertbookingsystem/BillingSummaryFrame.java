package com.mycompany.concertbookingsystem;

import com.mycompany.concertbookingsystem.dao.BookingDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class BillingSummaryFrame extends JFrame {
    private final DatabaseManager dbManager = new DatabaseManager();
    private final BookingDAO bookingDAO = new BookingDAO(dbManager);
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);
    private final JLabel totalLabel = new JLabel("Total Billing: ₱0.00");

    public BillingSummaryFrame() {
        setTitle("Billing Summary");
        setSize(920, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Billing Summary", SwingConstants.LEFT);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        tableModel.setColumnIdentifiers(new Object[]{"Booking ID", "Customer", "Event", "Quantity", "Unit Price", "Total", "Status"});
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(6, 6));
        JButton printBtn = new JButton("Print Summary");
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Print Summary placeholder."));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(printBtn);
        bottom.add(buttonPanel, BorderLayout.EAST);
        bottom.add(totalLabel, BorderLayout.WEST);
        add(bottom, BorderLayout.SOUTH);

        loadBillingDetails();
    }

    private void loadBillingDetails() {
        tableModel.setRowCount(0);
        double grandTotal = 0;
        List<Map<String, Object>> rows = bookingDAO.getAllBookings();
        for (Map<String, Object> row : rows) {
            int qty = ((Number) row.getOrDefault("quantity", 0)).intValue();
            double totalPrice = ((Number) row.getOrDefault("ticket_price", 0.0)).doubleValue();
            double unitPrice = qty == 0 ? 0 : totalPrice / qty;
            tableModel.addRow(new Object[]{row.get("id"), row.get("customer_name"), row.get("concert_name"), qty, String.format("₱%.2f", unitPrice), String.format("₱%.2f", totalPrice), row.get("status")});
            grandTotal += totalPrice;
        }
        totalLabel.setText(String.format("Total Billing: ₱%.2f", grandTotal));
    }
}
