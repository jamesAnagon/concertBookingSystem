package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginFrame extends JFrame {

    private DatabaseManager dbManager;
    
    // Form input fields
    private JTextField loginUserField, signUpUserField;
    private JPasswordField loginPassField, signUpPassField, signUpConfirmPassField;
    
    // Switchable view components
    private JPanel cardsContainer;
    private CardLayout cardLayout;

    // Brand Colors
    private final Color PRIMARY_COLOR = Color.decode("#1800ad");
    private final Color SECONDARY_COLOR = Color.WHITE;
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color TEXT_COLOR = new Color(20, 20, 140);
    private final Color MUTED_TEXT = Color.decode("#C7C2EB");
    private JPanel currentBuildingCard;

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
        rootPanel.setBackground(PRIMARY_COLOR);

        // 2. HEADER NAVBAR SECTION (Top Alignment)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 0, 40));

        JLabel brandLogo = new JLabel("🎵 Bookist");
        brandLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        brandLogo.setForeground(SECONDARY_COLOR);
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


    // LOG IN BUILDER
    private JPanel buildLoginCard() {
        JPanel card = createBaseCardPanel(250);
        currentBuildingCard = card;

        addFormLabel("Username / Email", 30);
        loginUserField = (JTextField) addFormField(new JTextField(), 55);

        addFormLabel("Password", 105);
        loginPassField = (JPasswordField) addFormField(new JPasswordField(), 130);

        PasswordVisibilityToggle loginShowPass = new PasswordVisibilityToggle(
            "Show Password", TEXT_COLOR, SECONDARY_COLOR, loginPassField
        );
        loginShowPass.setBounds(35, 175, 150, 20);
        card.add(loginShowPass);

        JButton loginBtn = addActionButton("LOGIN", 205);
        loginBtn.addActionListener(e -> handleLoginLogic());

        JPanel footerPanel = buildFooterPanel("Need an account?", "SIGN UP", e -> cardLayout.show(cardsContainer, "SIGNUP_CARD"));
        cardsContainer.revalidate();
        cardsContainer.repaint();

        return buildWrappedCard("LOGIN", card, footerPanel);
    }
    
    //SIGN UP 
    private JPanel buildSignUpCard() {
        JPanel card = createBaseCardPanel(350);
        currentBuildingCard = card;

        addFormLabel("Username / email", 30);
        signUpUserField = (JTextField) addFormField(new JTextField(), 55);

        addFormLabel("Password", 105);
        signUpPassField = (JPasswordField) addFormField(new JPasswordField(), 130);

        addFormLabel("Confirm Password", 180);
        signUpConfirmPassField = (JPasswordField) addFormField(new JPasswordField(), 205);

        PasswordVisibilityToggle showPassCheckBox = new PasswordVisibilityToggle(
            "Show Password", TEXT_COLOR, SECONDARY_COLOR, signUpPassField, signUpConfirmPassField
        );
        showPassCheckBox.setBounds(35, 250, 150, 20);
        card.add(showPassCheckBox);

        JButton signUpBtn = addActionButton("SIGN UP", 280);
        signUpBtn.addActionListener(e -> handleSignUpLogic());

        JPanel footerPanel = buildFooterPanel("Already a user?", "LOGIN", e -> cardLayout.show(cardsContainer, "LOGIN_CARD"));
        cardsContainer.revalidate();
        cardsContainer.repaint();

        return buildWrappedCard("SIGN UP", card, footerPanel);
    }


    
    // --- DESIGN COMPONENT DECORATORS ---

    private JPanel createBaseCardPanel(int height) {
        JPanel panel = new JPanel(null); // Absolute layout inside card
        panel.setBackground(SECONDARY_COLOR);
        panel.setPreferredSize(new Dimension(350, height)); // Uses the custom height parameter
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));
        return panel;
    }


    private void styleInputLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXT_COLOR);
    }

    private void styleInputField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleFormButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(SECONDARY_COLOR);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleHyperlinkButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(SECONDARY_COLOR);
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
        String confirmPass = new String(signUpConfirmPassField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        
        if (pass.equals(confirmPass)) {
            if (dbManager.registerUser(user, pass)) {
                JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
                signUpUserField.setText("");
                signUpPassField.setText("");
                cardLayout.show(cardsContainer, "LOGIN_CARD");
            } else {
                JOptionPane.showMessageDialog(this, "Username taken or database error.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Confirm password must be the same");
        }
    }

    private void confirmQuit() {
        int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit Application", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
     // --- REFACTORING HELPER METHODS ---

    // 1. Helper to create, style, position, and add labels
    private JLabel addFormLabel(String text, int y) {
        JLabel label = new JLabel(text);
        styleInputLabel(label);
        label.setBounds(35, y, 280, 20);
        
        currentBuildingCard.add(label); 
        return label;
    }

    // 2. Helper to style, position, and add text/password fields
    private JTextField addFormField(JTextField field, int y) {
        styleInputField(field);
        field.setBounds(35, y, 280, 35);
        
        currentBuildingCard.add(field); 
        return field;
    }
    // 3. Main Action Button Helper (LOGIN / SIGN UP)
    private JButton addActionButton(String text, int y) {
        JButton button = new JButton(text);
        styleFormButton(button);
        button.setBounds(35, y, 280, 40);
        
        currentBuildingCard.add(button); // Placed onto the active card
        
        return button;
    }

    // 4. Muted Footer Text Helper ("Need an account? " / "Already a user? ")
    private JLabel addFooterText(String text, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(MUTED_TEXT);
        label.setBounds(x, y, width, 20);
        
        currentBuildingCard.add(label); // Placed onto the active card
        
        return label;
    }

    // 5. Hyperlink Switch Button Helper (SIGN UP / LOGIN links)
    private JButton addSwitchButton(String text, int x, int y, int width) {
        JButton button = new JButton(text);
        styleHyperlinkButton(button);
        button.setBounds(x, y, width, 20);
        currentBuildingCard.add(button); // Placed onto the active card
        return button;
    }

    private JPanel buildFooterPanel(String text, String buttonText, ActionListener action) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        footer.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(Color.WHITE);

        JButton switchButton = new JButton(buttonText);
        styleHyperlinkButton(switchButton);
        switchButton.addActionListener(action);

        footer.add(label);
        footer.add(switchButton);
        return footer;
    }

    // 6. Helper to combine your two card wrappers into one single method
    private JPanel buildWrappedCard(String titleText, JPanel innerCard, JPanel footerPanel) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        innerCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wrapper.add(title);
        wrapper.add(Box.createVerticalStrut(16));
        wrapper.add(innerCard);
        wrapper.add(Box.createVerticalStrut(18));
        wrapper.add(footerPanel);
        return wrapper;
    }
}
