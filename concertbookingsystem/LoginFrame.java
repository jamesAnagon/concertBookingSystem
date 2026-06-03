package com.mycompany.concertbookingsystem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginFrame extends JFrame {

    private final AuthManager authManager;

    private JTextField loginUserField;
    private JTextField signUpUserField;
    private JPasswordField loginPassField;
    private JPasswordField signUpPassField;
    private JPasswordField signUpConfirmPassField;
    private JTextField promoCodeField;

    private JPanel cardsContainer;
    private CardLayout cardLayout;
    private JPanel currentBuildingCard;

    private final Color PRIMARY_COLOR = Color.decode("#1800ad");
    private final Color SECONDARY_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(20, 20, 140);

    public LoginFrame() {
        authManager = new AuthManager();

        setTitle("BooKist");
        setSize(1100, 610);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmQuit();
            }
        });

        JPanel rootPanel = new JPanel(null);
        rootPanel.setBackground(SECONDARY_COLOR);

        LoginBrandPanel brandPanel = new LoginBrandPanel();
        brandPanel.setBounds(0, 0, 510, 610);
        rootPanel.add(brandPanel);

        JLabel cornerMark = new JLabel("1");
        cornerMark.setFont(new Font("Serif", Font.BOLD, 56));
        cornerMark.setForeground(PRIMARY_COLOR);
        cornerMark.setBounds(1028, 4, 50, 60);
        rootPanel.add(cornerMark);

        cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);
        cardsContainer.setBackground(SECONDARY_COLOR);
        cardsContainer.setBounds(545, 78, 500, 470);
        cardsContainer.add(buildLoginCard(), "LOGIN_CARD");
        cardsContainer.add(buildSignUpCard(), "SIGNUP_CARD");
        rootPanel.add(cardsContainer);

        add(rootPanel);
        cardLayout.show(cardsContainer, "LOGIN_CARD");
    }

    private JPanel buildLoginCard() {
        JPanel card = createBaseCardPanel();
        currentBuildingCard = card;

        addTitle("LOG IN", 34);
        addFormLabel("Username:", 112);
        loginUserField = addFormField(new JTextField(), 142);
        applyInputConstraints(loginUserField, true, true, true);

        addFormLabel("Password:", 205);
        loginPassField = (JPasswordField) addFormField(new JPasswordField(), 235);
        applyInputConstraints(loginPassField, true, true, false);

        PasswordVisibilityToggle showPassword = new PasswordVisibilityToggle(
                "Show Password", TEXT_COLOR, SECONDARY_COLOR, loginPassField
        );
        showPassword.setBounds(118, 287, 160, 20);
        card.add(showPassword);

        JButton loginBtn = addActionButton("LOGIN", 320);
        loginBtn.addActionListener(e -> handleLoginLogic());

        JPanel footerPanel = buildFooterPanel(
                "NOT A USER YET?", "SIGN UP", e -> cardLayout.show(cardsContainer, "SIGNUP_CARD")
        );
        footerPanel.setBounds(74, 414, 350, 45);
        card.add(footerPanel);

        return card;
    }

    private JPanel buildSignUpCard() {
        JPanel card = createBaseCardPanel();
        currentBuildingCard = card;

        addTitle("CREATE YOUR BooKist ACCOUNT", 30);
        addFormLabel("Username:", 82);
        signUpUserField = addFormField(new JTextField(), 108);
        applyInputConstraints(signUpUserField, true, true, true);

        addFormLabel("Password:", 166);
        signUpPassField = (JPasswordField) addFormField(new JPasswordField(), 192);
        applyInputConstraints(signUpPassField, true, true, false);

        addFormLabel("Confirm Password", 250);
        signUpConfirmPassField = (JPasswordField) addFormField(new JPasswordField(), 276);
        applyInputConstraints(signUpConfirmPassField, true, true, false);

        promoCodeField = new JTextField();

        PasswordVisibilityToggle showPassword = new PasswordVisibilityToggle(
                "Show Password", TEXT_COLOR, SECONDARY_COLOR, signUpPassField, signUpConfirmPassField
        );
        showPassword.setBounds(118, 330, 160, 20);
        card.add(showPassword);

        JButton signUpBtn = addActionButton("SIGN UP", 360);
        signUpBtn.addActionListener(e -> handleSignUpLogic());

        JPanel footerPanel = buildFooterPanel(
                "Already have an account?", "LOG IN", e -> cardLayout.show(cardsContainer, "LOGIN_CARD")
        );
        footerPanel.setBounds(72, 414, 370, 45);
        card.add(footerPanel);

        return card;
    }

    private JPanel createBaseCardPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(SECONDARY_COLOR);
        panel.setPreferredSize(new Dimension(500, 470));
        return panel;
    }

    private void addTitle(String text, int fontSize) {
        JLabel title = new JLabel(text, SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, fontSize));
        title.setForeground(PRIMARY_COLOR);
        title.setBounds(0, 0, 500, 70);
        currentBuildingCard.add(title);
    }

    private JLabel addFormLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 19));
        label.setForeground(TEXT_COLOR);
        label.setBounds(110, y, 280, 22);
        currentBuildingCard.add(label);
        return label;
    }

    private JTextField addFormField(JTextField field, int y) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 17));
        field.setForeground(TEXT_COLOR);
        field.setOpaque(false);
        field.setBorder(new RoundedBorder(PRIMARY_COLOR, 2, 34));
        field.setBounds(108, y, 315, 43);
        currentBuildingCard.add(field);
        return field;
    }

    private JButton addActionButton(String text, int y) {
        JButton button = new RoundedButton(text, PRIMARY_COLOR);
        button.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 23));
        button.setForeground(SECONDARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBounds(108, y, 315, 47);
        currentBuildingCard.add(button);
        return button;
    }

    private JPanel buildFooterPanel(String text, String buttonText, ActionListener action) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        footer.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 13));
        label.setForeground(TEXT_COLOR);

        JButton switchButton = new JButton(buttonText);
        switchButton.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        switchButton.setForeground(PRIMARY_COLOR);
        switchButton.setContentAreaFilled(false);
        switchButton.setBorderPainted(false);
        switchButton.setFocusPainted(false);
        switchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchButton.addActionListener(action);

        footer.add(label);
        footer.add(switchButton);
        return footer;
    }

    private void applyInputConstraints(JTextComponent field, boolean noSpaces, boolean autoLowercase, boolean allowOnlyAlphanumeric) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void normalizeText() {
                SwingUtilities.invokeLater(() -> {
                    String original = field.getText();
                    String normalized = original;
                    if (noSpaces) {
                        normalized = normalized.replace(" ", "");
                    }
                    if (autoLowercase) {
                        normalized = normalized.toLowerCase();
                    }
                    if (allowOnlyAlphanumeric) {
                        normalized = normalized.replaceAll("[^a-z0-9]", "");
                    }
                    if (!normalized.equals(original)) {
                        int pos = field.getCaretPosition();
                        field.setText(normalized);
                        field.setCaretPosition(Math.max(0, Math.min(pos - (original.length() - normalized.length()), normalized.length())));
                    }
                });
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                normalizeText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                normalizeText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                normalizeText();
            }
        });
    }

    private void handleLoginLogic() {
        String user = loginUserField.getText().trim();
        String pass = new String(loginPassField.getPassword()).trim();
        authManager.login(this, user, pass);
    }

    private void handleSignUpLogic() {
        String user = signUpUserField.getText().trim();
        String pass = new String(signUpPassField.getPassword()).trim();
        String confirmPass = new String(signUpConfirmPassField.getPassword()).trim();
        String promoCode = promoCodeField.getText().trim();

        if (authManager.signUp(this, user, pass, confirmPass, promoCode)) {
            signUpUserField.setText("");
            signUpPassField.setText("");
            signUpConfirmPassField.setText("");
            promoCodeField.setText("");
            cardLayout.show(cardsContainer, "LOGIN_CARD");
        }
    }

    private void confirmQuit() {
        int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit Application", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private class BrandPanel extends JPanel {
        BrandPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(PRIMARY_COLOR);
            g2.fillRoundRect(-50, -45, 560, 705, 80, 80);

            g2.setColor(SECONDARY_COLOR);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(92, 70, 310, 136);
            g2.drawLine(188, 70, 188, 206);
            for (int y = 84; y <= 178; y += 29) {
                g2.setColor(PRIMARY_COLOR);
                g2.fillOval(84, y, 18, 22);
                g2.fillOval(392, y, 18, 22);
                g2.setColor(SECONDARY_COLOR);
                g2.drawOval(84, y, 18, 22);
                g2.drawOval(392, y, 18, 22);
            }
            g2.setStroke(new BasicStroke(2));
            for (int y = 88; y <= 190; y += 5) {
                g2.drawLine(119, y, 170, y);
            }

            g2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 46));
            g2.drawString("   BooKist", 138, 291);
            g2.setFont(new Font("SansSerif", Font.BOLD, 26));
            g2.drawString("Ticket booking System", 109, 394);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Booking Tickets way easier!!", 144, 457);
            g2.setFont(new Font("Serif", Font.BOLD, 58));
            g2.drawString("L", 45, 575);
            g2.dispose();
        }
    }

    private static class RoundedButton extends JButton {
        private final Color color;

        RoundedButton(String text, Color color) {
            super(text);
            this.color = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(7, 14, 7, 14);
        }
    }
}
