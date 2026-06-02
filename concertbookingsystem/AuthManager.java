package com.mycompany.concertbookingsystem;

import javax.swing.*;

public class AuthManager {
    private final DatabaseManager dbManager;
    private final NavigationManager navigationManager;

    public AuthManager() {
        dbManager = new DatabaseManager();
        navigationManager = new NavigationManager();
    }

    public boolean login(JFrame currentFrame, String username, String password) {
        String validationMessage = InputValidator.validateUsernameAndPassword(username, password);
        if (validationMessage != null) {
            JOptionPane.showMessageDialog(currentFrame, validationMessage);
            return false;
        }

        if (!dbManager.loginUser(username, password)) {
            JOptionPane.showMessageDialog(currentFrame, "Invalid credentials. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(currentFrame, "Login Successful! Welcome " + username);
        UserSession.login(username);
        navigationManager.showDashboardFrame(currentFrame);
        return true;
    }

    public boolean signUp(JFrame currentFrame, String username, String password, String confirmPassword, String promoCode) {
        String validationMessage = InputValidator.validateUsernameAndPassword(username, password);
        if (validationMessage != null) {
            JOptionPane.showMessageDialog(currentFrame, validationMessage);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(currentFrame, "Confirm password must be the same");
            return false;
        }

        if (!dbManager.registerUser(username, password)) {
            JOptionPane.showMessageDialog(currentFrame, "Username taken or database error.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String message = "Account created successfully! You can now log in.";
        if (!promoCode.isEmpty()) {
            message = "Promo code '" + promoCode + "' applied. Account created successfully! You can now log in.";
        }
        JOptionPane.showMessageDialog(currentFrame, message);
        return true;
    }

    public void logout(JFrame currentFrame) {
        UserSession.logout();
        navigationManager.showLoginFrame(currentFrame);
    }
}
