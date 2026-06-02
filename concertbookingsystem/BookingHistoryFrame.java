package com.mycompany.concertbookingsystem;

import com.mycompany.concertbookingsystem.dao.BookingDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class BookingHistoryFrame extends JFrame {
    private final DatabaseManager dbManager = new DatabaseManager();
    private final BookingDAO bookingDAO = new BookingDAO(dbManager);
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);
    private final TableRowSorter<DefaultTableModel> sorter;

    private final JTextField customerFilter = new JTextField();
    private final JTextField concertFilter = new JTextField();
    private final JTextField dateFilter = new JTextField();

    public BookingHistoryFrame() {
        setTitle("Booking History");
        setSize(980, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Booking History", SwingConstants.LEFT);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        tableModel.setColumnIdentifiers(new Object[]{"ID", "Customer", "Event", "Date", "Seat Type", "Quantity", "Total Price", "Status"});
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new GridLayout(1, 7, 8, 8));
        filterPanel.add(new JLabel("Customer:"));
        filterPanel.add(customerFilter);
        filterPanel.add(new JLabel("Concert:"));
        filterPanel.add(concertFilter);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(dateFilter);
        JButton searchBtn = new JButton("Filter");
        searchBtn.addActionListener(e -> applyFilter());
        filterPanel.add(searchBtn);
        add(filterPanel, BorderLayout.SOUTH);

        loadBookings();
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> rows = bookingDAO.getAllBookings();
        for (Map<String, Object> row : rows) {
            tableModel.addRow(new Object[]{row.get("id"), row.get("customer_name"), row.get("concert_name"), row.get("concert_date"), row.get("seat_type"), row.get("quantity"), row.get("ticket_price"), row.get("status")});
        }
    }

    private void applyFilter() {
        String customerText = customerFilter.getText().trim();
        String concertText = concertFilter.getText().trim();
        String dateText = dateFilter.getText().trim();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String customer = entry.getStringValue(1).toLowerCase();
                String concert = entry.getStringValue(2).toLowerCase();
                String date = entry.getStringValue(3).toLowerCase();
                return customer.contains(customerText.toLowerCase())
                        && concert.contains(concertText.toLowerCase())
                        && date.contains(dateText.toLowerCase());
            }
        });
    }
}
