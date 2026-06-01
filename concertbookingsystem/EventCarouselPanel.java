package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class EventCarouselPanel extends JPanel {
    private final String[] events;
    private final String[] dates;
    private final JPanel displayPanel;
    private final Color primaryColor;
    private final Color secondaryColor;
    private int startIndex = 0;

    public EventCarouselPanel(String[] events, String[] dates, Color primaryColor, Color secondaryColor, Consumer<String> buyAction) {
        this.events = events;
        this.dates = dates;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;

        setLayout(new BorderLayout());
        setBackground(new Color(248, 248, 252));
        setOpaque(true);

        JButton leftArrow = createArrowButton("◀");
        JButton rightArrow = createArrowButton("▶");

        displayPanel = new JPanel(new GridLayout(1, 3, 30, 10));
        displayPanel.setBackground(getBackground());
        displayPanel.setPreferredSize(new Dimension(900, 180));

        leftArrow.addActionListener(e -> {
            startIndex = (startIndex - 1 + events.length) % events.length;
            updateDisplay(buyAction);
        });

        rightArrow.addActionListener(e -> {
            startIndex = (startIndex + 1) % events.length;
            updateDisplay(buyAction);
        });

        JPanel carouselWrap = new JPanel(new BorderLayout());
        carouselWrap.setBackground(getBackground());
        carouselWrap.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        carouselWrap.add(leftArrow, BorderLayout.WEST);
        carouselWrap.add(displayPanel, BorderLayout.CENTER);
        carouselWrap.add(rightArrow, BorderLayout.EAST);

        add(carouselWrap, BorderLayout.CENTER);
        updateDisplay(buyAction);
    }

    private JButton createArrowButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 28));
        button.setForeground(primaryColor);
        return button;
    }

    private JPanel createCard(String eventName, String date, Consumer<String> buyAction) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 40, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        card.setPreferredSize(new Dimension(260, 150));
        card.setLayout(new BorderLayout());

        JLabel nameLbl = new JLabel(eventName, SwingConstants.CENTER);
        nameLbl.setForeground(secondaryColor);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel dateLbl = new JLabel(date, SwingConstants.CENTER);
        dateLbl.setForeground(new Color(200, 200, 230));
        dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton buy = new JButton("BUY TICKETS");
        buy.setBackground(primaryColor);
        buy.setForeground(secondaryColor);
        buy.setFocusPainted(false);
        buy.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        buy.addActionListener(e -> buyAction.accept(eventName));

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(nameLbl, BorderLayout.NORTH);
        content.add(dateLbl, BorderLayout.CENTER);
        JPanel btnRow = new JPanel();
        btnRow.setOpaque(false);
        btnRow.add(buy);
        content.add(btnRow, BorderLayout.SOUTH);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void updateDisplay(Consumer<String> buyAction) {
        displayPanel.removeAll();
        int n = events.length;
        for (int i = 0; i < 3; i++) {
            int idx = (startIndex + i) % n;
            displayPanel.add(createCard(events[idx], dates[idx], buyAction));
        }
        displayPanel.revalidate();
        displayPanel.repaint();
    }
}
