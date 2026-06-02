package com.mycompany.concertbookingsystem;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {
    private final Color borderColor;
    private final int borderThickness;
    private final int cornerRadius;

    public RoundedBorder(Color borderColor, int borderThickness, int cornerRadius) {
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
        this.cornerRadius = cornerRadius;
    }

    @Override
    public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(borderColor);
        graphics2D.setStroke(new BasicStroke(borderThickness));
        graphics2D.drawRoundRect(x + 1, y + 1, width - 3, height - 3, cornerRadius, cornerRadius);
        graphics2D.dispose();
    }

    @Override
    public Insets getBorderInsets(Component component) {
        return new Insets(7, 14, 7, 14);
    }
}
