package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;

public class PasswordVisibilityToggle extends JCheckBox {

    public PasswordVisibilityToggle(String text, Color textColor, Color bgColor, JPasswordField... fields) {
        super(text);
        
        // Apply brand styles
        setFont(new Font("SansSerif", Font.PLAIN, 12));
        setForeground(textColor);
        setBackground(bgColor);
        setFocusPainted(false);

        // toggle action logic
        this.addActionListener(e -> {
            char echoChar = this.isSelected() ? (char) 0 : '•';
            for (JPasswordField field : fields) {
                if (field != null) {
                    field.setEchoChar(echoChar);
                }
            }
        });
    }
}
