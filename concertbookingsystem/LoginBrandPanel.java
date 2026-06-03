package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;

public class LoginBrandPanel extends JPanel {
    public LoginBrandPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(AppTheme.PRIMARY_BLUE);
        graphics2D.fillRoundRect(-50, -45, 560, 705, 80, 80);

        drawTicketIcon(graphics2D);
        drawBrandText(graphics2D);
        graphics2D.dispose();
    }

    private void drawTicketIcon(Graphics2D graphics2D) {
        graphics2D.setColor(AppTheme.WHITE);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(92, 70, 310, 136);
        graphics2D.drawLine(188, 70, 188, 206);

        for (int y = 84; y <= 178; y += 29) {
            graphics2D.setColor(AppTheme.PRIMARY_BLUE);
            graphics2D.fillOval(84, y, 18, 22);
            graphics2D.fillOval(392, y, 18, 22);
            graphics2D.setColor(AppTheme.WHITE);
            graphics2D.drawOval(84, y, 18, 22);
            graphics2D.drawOval(392, y, 18, 22);
        }

        graphics2D.setStroke(new BasicStroke(2));
        for (int y = 88; y <= 190; y += 5) {
            graphics2D.drawLine(119, y, 170, y);
        }
    }

    private void drawBrandText(Graphics2D graphics2D) {
        graphics2D.setColor(AppTheme.WHITE);
        graphics2D.setFont(AppTheme.italicSerif(46));
        graphics2D.drawString("   BooKist", 138, 291);
        graphics2D.setFont(AppTheme.boldSans(26));
        graphics2D.drawString("Ticket booking System", 109, 394);
        graphics2D.setFont(AppTheme.boldSans(14));
        graphics2D.drawString("Booking Tickets way easier!!", 144, 457);
        graphics2D.setFont(new Font("Serif", Font.BOLD, 58));
        graphics2D.drawString("L", 45, 575);
    }
}
