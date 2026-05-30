package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginFrame extends JFrame {

    private DatabaseManager dbManager;
    
    // Form input fields
    private JTextField loginUserField, signUpUserField;
    private JPasswordField loginPassField, signUpPassField;
    
    // Switchable view components
    private JPanel cardsContainer;
    private CardLayout cardLayout;
    
    // Harmonized Brand Colors
    private final Color BACKGROUND_BLUE = new Color(24, 119, 242);
    private final Color CARD_WHITE = Color.WHITE;
    private final Color PINK_ACCENT = new Color(254, 44, 85);
    private final Color TEXT_DARK = new Color(51, 51, 51);
    private final Color TEXT_LIGHT = new Color(119, 119, 119);

    public LoginFrame() {
        dbManager = new DatabaseManager();
        setTitle("Concert Sphere - Authentication");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Launch Fullscreen
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Window close confirmation interceptor
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmQuit();
            }
        });

        // 1. Root main frame wrapper panel
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(BACKGROUND_BLUE);

        // 2. HEADER NAVBAR SECTION (Top Alignment)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 0, 40));

        JLabel brandLogo = new JLabel("🎵 Bookist");
        brandLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        brandLogo.setForeground(Color.WHITE);
        headerPanel.add(brandLogo, BorderLayout.WEST);
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // 3. CENTER CONTAINER SECTION (Holds Card Module)
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);
        cardsContainer.setOpaque(false);

        // Instantiate card components
        cardsContainer.add(buildLoginCard(), "LOGIN_CARD");
        cardsContainer.add(buildSignUpCard(), "SIGNUP_CARD");
        centerWrapper.add(cardsContainer);
        rootPanel.add(centerWrapper, BorderLayout.CENTER);

        add(rootPanel);
        cardLayout.show(cardsContainer, "LOGIN_CARD");
    }

    // --- FORM VIEW BUILDERS ---
    
    private JPanel buildLoginCard() {
        JPanel card = createBaseCardPanel();

        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setBounds(35, 30, 100, 30);
        card.add(title);

        JLabel userLabel = new JLabel("Username / Email");
        styleInputLabel(userLabel);
        userLabel.setBounds(35, 80, 280, 20);
        card.add(userLabel);

        loginUserField = new JTextField();
        styleInputField(loginUserField);
        loginUserField.setBounds(35, 105, 280, 35);
        card.add(loginUserField);

        JLabel passLabel = new JLabel("Password");
        styleInputLabel(passLabel);
        passLabel.setBounds(35, 155, 280, 20);
        card.add(passLabel);

        loginPassField = new JPasswordField();
        styleInputField(loginPassField);
        loginPassField.setBounds(35, 180, 280, 35);
        card.add(loginPassField);

        JButton loginBtn = new JButton("LOGIN");
        styleFormButton(loginBtn);
        loginBtn.setBounds(35, 240, 280, 40);
        card.add(loginBtn);

        JLabel footerText = new JLabel("Need an account? ");
        footerText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footerText.setForeground(TEXT_LIGHT);
        footerText.setBounds(65, 305, 130, 20);
        card.add(footerText);

        JButton switchToSignUpBtn = new JButton("SIGN UP");
        styleHyperlinkButton(switchToSignUpBtn);
        switchToSignUpBtn.setBounds(180, 305, 110, 20);
        card.add(switchToSignUpBtn);

        loginBtn.addActionListener(e -> handleLoginLogic());
        switchToSignUpBtn.addActionListener(e -> cardLayout.show(cardsContainer, "SIGNUP_CARD"));

        return card;
    }

    private JPanel buildSignUpCard() {
        JPanel card = createBaseCardPanel();

        JLabel title = new JLabel("SIGN UP");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setBounds(35, 30, 120, 30);
        card.add(title);

        JLabel userLabel = new JLabel("Username / Email");
        styleInputLabel(userLabel);
        userLabel.setBounds(35, 80, 280, 20);
        card.add(userLabel);

        signUpUserField = new JTextField();
        styleInputField(signUpUserField);
        signUpUserField.setBounds(35, 105, 280, 35);
        card.add(signUpUserField);

        JLabel passLabel = new JLabel("Password");
        styleInputLabel(passLabel);
        passLabel.setBounds(35, 155, 280, 20);
        card.add(passLabel);

        signUpPassField = new JPasswordField();
        styleInputField(signUpPassField);
        signUpPassField.setBounds(35, 180, 280, 35);
        card.add(signUpPassField);

        JButton signUpBtn = new JButton("SIGN UP");
        styleFormButton(signUpBtn);
        signUpBtn.setBounds(35, 240, 280, 40);
        card.add(signUpBtn);

        JLabel footerText = new JLabel("Already a user? ");
        footerText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footerText.setForeground(TEXT_LIGHT);
        footerText.setBounds(70, 305, 120, 20);
        card.add(footerText);

        JButton switchToLoginBtn = new JButton("LOGIN");
        styleHyperlinkButton(switchToLoginBtn);
        switchToLoginBtn.setBounds(175, 305, 100, 20);
        card.add(switchToLoginBtn);

        signUpBtn.addActionListener(e -> handleSignUpLogic());
        switchToLoginBtn.addActionListener(e -> cardLayout.show(cardsContainer, "LOGIN_CARD"));

        return card;
    }

    // --- DESIGN COMPONENT DECORATORS ---

    private JPanel createBaseCardPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(CARD_WHITE);
        panel.setPreferredSize(new Dimension(350, 360));
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));
        return panel;
    }

    private void styleInputLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);
    }

    private void styleInputField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleFormButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PINK_ACCENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleHyperlinkButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(PINK_ACCENT);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
    }

    // --- AUTHENTICATION ACTION LOGIC ---

    private void handleLoginLogic() {
        String user = loginUserField.getText().trim();
        String pass = new String(loginPassField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        if (dbManager.loginUser(user, pass)) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + user);
            HomePage.isLoggedIn = true;
            HomePage.currentUsername = user;
            this.dispose();
            new MainDashboardFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignUpLogic() {
        String user = signUpUserField.getText().trim();
        String pass = new String(signUpPassField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        if (dbManager.registerUser(user, pass)) {
            JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
            signUpUserField.setText("");
            signUpPassField.setText("");
            cardLayout.show(cardsContainer, "LOGIN_CARD");
        } else {
            JOptionPane.showMessageDialog(this, "Username taken or database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmQuit() {
        int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit Application", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
