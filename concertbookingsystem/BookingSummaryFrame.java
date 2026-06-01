package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class BookingSummaryFrame extends JFrame {

    private final Color PRIMARY_COLOR = Color.decode("#1800ad");
    private final Color SECONDARY_COLOR = Color.WHITE;
    private final Color SURFACE = new Color(245, 245, 251);
    private final Color CARD_BG = new Color(236, 238, 251);
    private final Color TEXT_HEADING = new Color(24, 24, 100);
    private final Color TEXT_BODY = new Color(68, 74, 132);
    private final JFrame owner;
    private final String ticketId;

    public BookingSummaryFrame(JFrame owner, String customer, String eventName, List<String> seats, double totalPrice) {
        this.owner = owner;
        setTitle("Booking Summary");
        setSize(900, 640);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        this.ticketId = createTicketId(eventName, seats.get(0), customer);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (owner != null) {
                    owner.setVisible(true);
                    owner.toFront();
                }
            }
        });

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(SURFACE);
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(true);
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel titleLabel = new JLabel("BOOKING CONFIRMED");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        header.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Your tickets are reserved and ready for the show.");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(224, 224, 255));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
        header.add(subtitleLabel);

        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        root.add(content, BorderLayout.CENTER);

        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);
        content.add(mainContent, BorderLayout.CENTER);

        // Left side: Summary Card
        JPanel summaryCard = new JPanel(new BorderLayout(0, 18));
        summaryCard.setOpaque(true);
        summaryCard.setBackground(CARD_BG);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));

        JLabel eventLabel = new JLabel(eventName);
        eventLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        eventLabel.setForeground(TEXT_HEADING);
        summaryCard.add(eventLabel, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridBagLayout());
        details.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(8, 0, 8, 0);

        JLabel customerLabel = new JLabel("Customer");
        customerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        customerLabel.setForeground(PRIMARY_COLOR.darker());
        details.add(customerLabel, gc);

        gc.gridy = 1;
        JLabel customerValue = new JLabel(customer);
        customerValue.setFont(new Font("SansSerif", Font.PLAIN, 16));
        customerValue.setForeground(TEXT_BODY);
        details.add(customerValue, gc);

        gc.gridy = 2;
        JLabel seatsLabel = new JLabel("Selected Seats");
        seatsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        seatsLabel.setForeground(PRIMARY_COLOR.darker());
        details.add(seatsLabel, gc);

        gc.gridy = 3;
        JPanel seatListPanel = new JPanel();
        seatListPanel.setOpaque(false);
        seatListPanel.setLayout(new BoxLayout(seatListPanel, BoxLayout.Y_AXIS));
        for (String seat : seats) {
            JLabel seatValue = new JLabel("• " + seat);
            seatValue.setFont(new Font("SansSerif", Font.PLAIN, 15));
            seatValue.setForeground(TEXT_BODY);
            seatListPanel.add(seatValue);
        }
        details.add(seatListPanel, gc);

        gc.gridy = 4;
        JLabel totalLabel = new JLabel("Total Due");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(PRIMARY_COLOR.darker());
        details.add(totalLabel, gc);

        gc.gridy = 5;
        JLabel totalValue = new JLabel(String.format("₱%.2f", totalPrice));
        totalValue.setFont(new Font("SansSerif", Font.BOLD, 28));
        totalValue.setForeground(PRIMARY_COLOR);
        details.add(totalValue, gc);

        summaryCard.add(details, BorderLayout.CENTER);

        mainContent.add(summaryCard);

        // Right side: E-Ticket Preview
        JPanel ticketCard = new JPanel(new BorderLayout(0, 16));
        ticketCard.setOpaque(true);
        ticketCard.setBackground(CARD_BG);
        ticketCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));

        JLabel ticketTitle = new JLabel("E-Ticket Preview");
        ticketTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        ticketTitle.setForeground(PRIMARY_COLOR.darker());
        ticketCard.add(ticketTitle, BorderLayout.NORTH);

        QrPreviewPanel qrPreview = new QrPreviewPanel();
        qrPreview.setSeed(ticketId);
        qrPreview.setPreferredSize(new Dimension(220, 220));
        ticketCard.add(qrPreview, BorderLayout.CENTER);

        JPanel ticketFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ticketFooter.setOpaque(false);
        JButton exportBtn = new JButton("Export Ticket");
        exportBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        exportBtn.setBackground(PRIMARY_COLOR);
        exportBtn.setForeground(SECONDARY_COLOR);
        exportBtn.setFocusPainted(false);
        exportBtn.addActionListener(e -> exportTicketImage(customer, eventName));
        ticketFooter.add(exportBtn);
        ticketCard.add(ticketFooter, BorderLayout.SOUTH);

        mainContent.add(ticketCard);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        JButton closeButton = new JButton("Done");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(SECONDARY_COLOR);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        footer.add(closeButton);
        content.add(footer, BorderLayout.SOUTH);
    }

    private String createTicketId(String eventName, String seat, String customer) {
        String baseEvent = eventName == null ? "UnknownEvent" : eventName.replaceAll("\\s*\\([^)]*\\)$", "");
        String sanitizedEvent = baseEvent.replaceAll("[^A-Za-z0-9]", "");
        if (seat.equals("N/A")) {
            return sanitizedEvent.isEmpty() ? "UNKNOWN_TICKET" : sanitizedEvent;
        }
        return seat + "_" + sanitizedEvent;
    }

    private void exportTicketImage(String customer, String eventName) {
        if (ticketId == null) {
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
        g2.drawString(eventName, 40, 220);

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("Customer", 40, 265);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.drawString(customer, 40, 295);

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("Ticket ID", 40, 340);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.drawString(ticketId, 40, 370);

        int qrX = 320;
        int qrY = 180;
        int qrSize = 180;
        g2.setColor(Color.WHITE);
        g2.fillRect(qrX - 10, qrY - 10, qrSize + 20, qrSize + 20);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(qrX - 10, qrY - 10, qrSize + 20, qrSize + 20);
        g2.setColor(Color.DARK_GRAY);
        QrPreviewPanel.drawMockQr(g2, ticketId, qrX, qrY, qrSize);

        g2.setColor(PRIMARY_COLOR);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(16, 136, image.getWidth() - 72, 528, 28, 28);
        g2.dispose();

        String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
        File downloadsDir = new File(downloadsPath);
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }
        File output = new File(downloadsDir, "ticket_" + ticketId.replaceAll("[^a-zA-Z0-9_-]", "_") + ".png");
        try {
            javax.imageio.ImageIO.write(image, "PNG", output);
            JOptionPane.showMessageDialog(this, "Ticket exported to " + output.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to export ticket: " + ex.getMessage());
        }
    }

    public static class QrPreviewPanel extends JPanel {
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
}
