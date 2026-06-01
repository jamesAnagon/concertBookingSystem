package com.mycompany.concertbookingsystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BookingPanel extends JPanel {
    private DatabaseManager dbManager;
    private CardLayout cardLayout;
    private JPanel cards;

    private final Color PRIMARY_COLOR = Color.decode("#1800ad");
    private final Color SECONDARY_COLOR = Color.WHITE;
    private final Color PANEL_BG = new Color(245, 245, 251);
    private final Color CARD_BG = new Color(248, 248, 252);
    private final Color TABLE_STRIPE = new Color(236, 238, 251);

    // View mode components
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private JCheckBox deleteModeCheckbox;
    private JButton deleteSelectedBtn;
    private JLabel qrTitleLabel;
    private JLabel qrDetailsLabel;
    private JButton printTicketBtn;
    private QrPreviewPanel qrPreviewPanel;

    private String currentTicketId;
    private String currentEventName;
    private String currentTicketType;
    private String currentTicketPrice;
    private String currentCustomerName;

    public BookingPanel() {
        dbManager = new DatabaseManager();
        setLayout(new BorderLayout());
        setBackground(PANEL_BG);

        // Top controls area
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(PRIMARY_COLOR);
        backBtn.setForeground(SECONDARY_COLOR);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        backBtn.addActionListener(e -> showDashboard());
        top.add(backBtn);
        add(top, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(buildCreateCard(), "CREATE");
        cards.add(buildViewCard(), "VIEW");

        add(cards, BorderLayout.CENTER);
        cardLayout.show(cards, "VIEW");
    }

    private JPanel buildCreateCard() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        c.gridx = 0; c.gridy = 0;

        JLabel lblType = new JLabel("Ticket Class:");
        lblType.setForeground(PRIMARY_COLOR.darker());
        p.add(lblType, c);

        c.gridx = 1;
        String[] strategies = {"Regular Ticket", "VIP Ticket"};
        JComboBox<String> choice = new JComboBox<>(strategies);
        choice.setBackground(SECONDARY_COLOR);
        choice.setForeground(Color.DARK_GRAY);
        p.add(choice, c);

        c.gridx = 0; c.gridy = 1;
        JLabel lblName = new JLabel("Customer Name:");
        lblName.setForeground(PRIMARY_COLOR.darker());
        p.add(lblName, c);
        c.gridx = 1;
        JTextField nameField = new JTextField(20);
        nameField.setText(HomePage.currentUsername != null ? HomePage.currentUsername : "");
        nameField.setBackground(SECONDARY_COLOR);
        nameField.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        p.add(nameField, c);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        JButton createBtn = new JButton("Create Booking");
        createBtn.setBackground(PRIMARY_COLOR);
        createBtn.setForeground(SECONDARY_COLOR);
        createBtn.setFocusPainted(false);
        createBtn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a customer name.");
                return;
            }
            int idx = choice.getSelectedIndex();
            if (idx == 0) {
                Ticket t = new RegularTicket(name, "Featured Concert", 120.0);
                dbManager.createBooking(t);
            } else {
                Ticket t = new VipTicket(name, "Featured Concert", 300.0, "VIP Lounge");
                dbManager.createBooking(t);
            }
            JOptionPane.showMessageDialog(this, "Booking created.");
            loadBookingsIntoTable();
            cardLayout.show(cards, "VIEW");
        });
        p.add(createBtn, c);

        return p;
    }

    private JPanel buildViewCard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        tableModel = new DefaultTableModel();
        bookingsTable = new JTable(tableModel) {
            @Override
            public Class getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                return Object.class;
            }
        };
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.setRowHeight(48);
        bookingsTable.setShowGrid(false);
        bookingsTable.setIntercellSpacing(new Dimension(0, 0));
        bookingsTable.setBackground(CARD_BG);
        bookingsTable.setForeground(Color.DARK_GRAY);
        bookingsTable.setSelectionBackground(new Color(212, 226, 255));
        bookingsTable.setSelectionForeground(Color.BLACK);
        bookingsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateQrPreview();
            }
        });

        JScrollPane sp = new JScrollPane(bookingsTable);
        sp.setPreferredSize(new Dimension(0, 360));

        JPanel detailsPanel = buildQrPane();
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, detailsPanel);
        split.setResizeWeight(0.7);
        split.setOneTouchExpandable(true);
        split.setDividerSize(8);
        p.add(split, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        bottom.setOpaque(false);
        deleteModeCheckbox = new JCheckBox("Delete Mode");
        deleteModeCheckbox.setBackground(CARD_BG);
        deleteModeCheckbox.setForeground(Color.DARK_GRAY);
        deleteModeCheckbox.addActionListener(e -> toggleDeleteMode(deleteModeCheckbox.isSelected()));
        bottom.add(deleteModeCheckbox);

        deleteSelectedBtn = new JButton("Delete Selected");
        styleActionButton(deleteSelectedBtn, false);
        deleteSelectedBtn.addActionListener(e -> deleteSelected());
        deleteSelectedBtn.setEnabled(false);
        bottom.add(deleteSelectedBtn);

        JButton newBooking = new JButton("New Booking");
        styleActionButton(newBooking, true);
        newBooking.addActionListener(e -> cardLayout.show(cards, "CREATE"));
        bottom.add(newBooking);

        p.add(bottom, BorderLayout.SOUTH);

        loadBookingsIntoTable();
        return p;
    }

    private JPanel buildQrPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("E-Ticket Preview"),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        panel.setBackground(new Color(248, 248, 252));

        qrTitleLabel = new JLabel("E-Ticket Preview", SwingConstants.CENTER);
        qrTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        qrTitleLabel.setForeground(PRIMARY_COLOR.darker());
        panel.add(qrTitleLabel, BorderLayout.NORTH);

        qrPreviewPanel = new QrPreviewPanel();
        qrPreviewPanel.setPreferredSize(new Dimension(240, 240));

        qrDetailsLabel = new JLabel("<html><div style='text-align:center;color:#333;'>No ticket selected</div></html>", SwingConstants.CENTER);
        qrDetailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setOpaque(false);
        content.add(qrPreviewPanel, BorderLayout.CENTER);
        content.add(qrDetailsLabel, BorderLayout.SOUTH);
        panel.add(content, BorderLayout.CENTER);

        printTicketBtn = new JButton("Print E-Ticket");
        styleActionButton(printTicketBtn, true);
        printTicketBtn.setEnabled(false);
        printTicketBtn.addActionListener(e -> exportTicketImage());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.add(printTicketBtn);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private void updateQrPreview() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow < 0) {
            qrTitleLabel.setText("Select a ticket to preview");
            qrDetailsLabel.setText("<html><div style='text-align:center;color:#333;'>No ticket selected</div></html>");
            qrPreviewPanel.setSeed(null);
            qrPreviewPanel.repaint();
            printTicketBtn.setEnabled(false);
            currentTicketId = null;
            return;
        }
        int modelRow = bookingsTable.convertRowIndexToModel(selectedRow);
        Object event = tableModel.getValueAt(modelRow, 3);
        Object ticketType = tableModel.getValueAt(modelRow, 5);
        Object price = tableModel.getValueAt(modelRow, 4);
        Object customer = tableModel.getValueAt(modelRow, 2);

        currentEventName = event != null ? event.toString() : "Unknown Event";
        currentTicketType = ticketType != null ? ticketType.toString() : "Ticket";
        currentTicketPrice = price != null ? price.toString() : "N/A";
        currentCustomerName = customer != null ? customer.toString() : "Guest";
        currentTicketId = createTicketId(currentEventName, currentTicketType, currentCustomerName);

        qrTitleLabel.setText("E-Ticket Preview");
        qrDetailsLabel.setText(String.format(
                "<html><div style='text-align:center;color:#333;'>Event: %s<br/>Seat ID: %s<br/>Ticket ID: %s</div></html>",
                currentEventName,
                extractSeatFromEvent(currentEventName),
                currentTicketId));
        qrPreviewPanel.setSeed(currentTicketId);
        qrPreviewPanel.repaint();
        printTicketBtn.setEnabled(true);
    }

    private void exportTicketImage() {
        if (currentTicketId == null) {
            JOptionPane.showMessageDialog(this, "Please select a ticket first.");
            return;
        }
        BufferedImage image = new BufferedImage(520, 720, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());

        g2.setColor(PRIMARY_COLOR);
        g2.fillRect(0, 0, image.getWidth(), 120);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.drawString("BOOKIST E-TICKET", 24, 56);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Enjoy your show!", 24, 86);

        g2.setColor(new Color(240, 240, 255));
        g2.fillRoundRect(20, 140, image.getWidth() - 40, 520, 28, 28);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("Event", 40, 190);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.drawString(currentEventName, 40, 220);

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("Customer", 40, 265);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.drawString(currentCustomerName, 40, 295);

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("Ticket Type", 40, 340);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.drawString(currentTicketType, 40, 370);

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("Price", 40, 415);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.drawString(currentTicketPrice, 40, 445);

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("Ticket ID", 40, 490);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.drawString(currentTicketId, 40, 520);

        int qrX = 320;
        int qrY = 180;
        int qrSize = 180;
        g2.setColor(Color.WHITE);
        g2.fillRect(qrX - 10, qrY - 10, qrSize + 20, qrSize + 20);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(qrX - 10, qrY - 10, qrSize + 20, qrSize + 20);
        g2.setColor(Color.DARK_GRAY);
        QrPreviewPanel.drawMockQr(g2, currentTicketId, qrX, qrY, qrSize);

        g2.setColor(PRIMARY_COLOR);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(16, 136, image.getWidth() - 72, 528, 28, 28);
        g2.dispose();

        String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
        File downloadsDir = new File(downloadsPath);
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }
        File output = new File(downloadsDir, "ticket_" + currentTicketId.replaceAll("[^a-zA-Z0-9_-]", "_") + ".png");
        try {
            ImageIO.write(image, "PNG", output);
            JOptionPane.showMessageDialog(this, "Ticket exported to " + output.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to export ticket: " + ex.getMessage());
        }
    }

    private String extractSeatFromEvent(String eventValue) {
        if (eventValue == null) return "N/A";
        int start = eventValue.lastIndexOf("(");
        int end = eventValue.lastIndexOf(")");
        if (start > 0 && end > start) {
            return eventValue.substring(start + 1, end);
        }
        return "N/A";
    }

    private String createTicketId(String eventValue, String ticketType, String customerName) {
        String seat = extractSeatFromEvent(eventValue);
        String baseEvent = eventValue == null ? "UnknownEvent" : eventValue.replaceAll("\\s*\\([^)]*\\)$", "");
        String sanitizedEvent = baseEvent.replaceAll("[^A-Za-z0-9]", "");
        if (seat.equals("N/A")) {
            return sanitizedEvent.isEmpty() ? "UNKNOWN_TICKET" : sanitizedEvent;
        }
        return seat + "_" + sanitizedEvent;
    }

    private static class QrPreviewPanel extends JPanel {
        private String seed;

        public void setSeed(String seed) {
            this.seed = seed;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(10, 10, getWidth() - 21, getHeight() - 21);
            int grid = 8;
            int cellSize = Math.min((getWidth() - 40) / grid, (getHeight() - 40) / grid);
            int startX = (getWidth() - (cellSize * grid)) / 2;
            int startY = (getHeight() - (cellSize * grid)) / 2;
            long value = seed != null ? seed.hashCode() : 0;
            for (int row = 0; row < grid; row++) {
                for (int col = 0; col < grid; col++) {
                    boolean filled = ((row * grid + col) % 2 == 0) ^ ((value >> (row * grid + col)) % 2 != 0);
                    g2.setColor(filled ? Color.BLACK : Color.WHITE);
                    g2.fillRect(startX + col * cellSize, startY + row * cellSize, cellSize, cellSize);
                }
            }
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(startX, startY, cellSize * grid, cellSize * grid);
            g2.dispose();
        }

        public static void drawMockQr(Graphics2D g2, String seed, int x, int y, int size) {
            long value = seed != null ? seed.hashCode() : 0;
            int grid = 8;
            int cellSize = size / grid;
            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, size, size);
            for (int row = 0; row < grid; row++) {
                for (int col = 0; col < grid; col++) {
                    boolean filled = ((row * grid + col) % 2 == 0) ^ ((value >> (row * grid + col)) % 2 != 0);
                    g2.setColor(filled ? Color.DARK_GRAY : Color.WHITE);
                    g2.fillRect(x + col * cellSize, y + row * cellSize, cellSize, cellSize);
                }
            }
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x, y, size, size);
        }
    }

    private void toggleDeleteMode(boolean on) {
        deleteSelectedBtn.setEnabled(on);
        // The checkbox column will be present; user can check boxes when on
    }

    private void deleteSelected() {
        int rows = tableModel.getRowCount();
        java.util.List<Integer> toDelete = new java.util.ArrayList<>();
        for (int i = rows - 1; i >= 0; i--) {
            Boolean sel = (Boolean) tableModel.getValueAt(i, 0);
            if (sel != null && sel) {
                Object idObj = tableModel.getValueAt(i, 1); // ID column
                if (idObj instanceof Number) {
                    toDelete.add(((Number) idObj).intValue());
                } else {
                    try { toDelete.add(Integer.parseInt(idObj.toString())); } catch (Exception ex) {}
                }
            }
        }
        if (toDelete.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bookings selected.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected bookings?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        for (Integer id : toDelete) {
            dbManager.deleteBooking(id);
        }
        loadBookingsIntoTable();
    }

    private void loadBookingsIntoTable() {
        String currentUser = HomePage.currentUsername != null ? HomePage.currentUsername : "";
        java.util.List<java.util.Map<String, Object>> rows = currentUser.isEmpty()
                ? java.util.Collections.emptyList()
                : dbManager.getBookingsForUser(currentUser);
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // First column is selection checkbox
        tableModel.addColumn("Select");
        tableModel.addColumn("ID");
        tableModel.addColumn("Customer");
        tableModel.addColumn("Event");
        tableModel.addColumn("Price");
        tableModel.addColumn("Ticket Type");
        tableModel.addColumn("Perk");

        for (Map<String, Object> r : rows) {
            tableModel.addRow(new Object[] {false, r.get("id"), r.get("customer_name"), r.get("concert_name"), r.get("ticket_price"), r.get("ticket_type"), r.get("special_perk")});
        }

        styleTableHeader();

        // hide id column visually but keep it accessible
        bookingsTable.getColumnModel().getColumn(1).setMinWidth(0);
        bookingsTable.getColumnModel().getColumn(1).setMaxWidth(0);
    }

    private void styleTableHeader() {
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(PRIMARY_COLOR);
        headerRenderer.setForeground(SECONDARY_COLOR);
        headerRenderer.setFont(new Font("SansSerif", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < bookingsTable.getColumnCount(); i++) {
            bookingsTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    private void styleActionButton(JButton button, boolean primary) {
        button.setBackground(primary ? PRIMARY_COLOR : new Color(230, 230, 245));
        button.setForeground(primary ? SECONDARY_COLOR : Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
    }

    private void showDashboard() {
        Container topLevel = this.getTopLevelAncestor();
        if (topLevel instanceof JFrame) {
            JFrame frame = (JFrame) topLevel;
            // Ask the parent to switch back to the main dashboard card if it supports it
            if (frame instanceof MainDashboardFrame) {
                ((MainDashboardFrame) frame).showHomeCard();
            }
        }
    }

    public void showCreateMode() {
        cardLayout.show(cards, "CREATE");
    }

    public void showViewMode(boolean deleteMode) {
        cardLayout.show(cards, "VIEW");
        deleteModeCheckbox.setSelected(deleteMode);
        toggleDeleteMode(deleteMode);
        loadBookingsIntoTable();
    }
}
