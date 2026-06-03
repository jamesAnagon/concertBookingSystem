package com.mycompany.concertbookingsystem;

public final class InputValidator {
    private InputValidator() {
    }

    public static String validateUsernameAndPassword(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return "Please fill in all fields.";
        }
        if (username.contains(" ")) {
            return "Username cannot contain spaces.";
        }
        if (password.contains(" ")) {
            return "Password cannot contain spaces.";
        }
        if (!username.matches("^[a-z0-9]+$")) {
            return "Username can only contain letters and numbers.";
        }
        return null;
    }
}
