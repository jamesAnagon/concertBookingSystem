package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;

public class PasswordVisibilityToggle extends JCheckBox {

    // Accept an array or varargs (...) of JPasswordFields so it can control multiple fields at once
    public PasswordVisibilityToggle(String text, Color textColor, Color bgColor, JPasswordField... fields) {
        super(text);
        
        // Apply your brand styles
        setFont(new Font("SansSerif", Font.PLAIN, 12));
        setForeground(textColor);
        setBackground(bgColor);
        setFocusPainted(false);

        // Add the toggle action logic
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
