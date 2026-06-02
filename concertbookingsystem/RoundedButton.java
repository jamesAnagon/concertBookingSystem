package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private final Color backgroundColor;

    public RoundedButton(String text, Color backgroundColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        setOpaque(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}
