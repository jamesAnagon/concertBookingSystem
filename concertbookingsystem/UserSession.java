package com.mycompany.concertbookingsystem;

public final class UserSession {
    public static boolean isLoggedIn = false;
    public static String currentUsername = "";

    private UserSession() {
    }

    public static void login(String username) {
        isLoggedIn = true;
        currentUsername = username;
    }

    public static void logout() {
        isLoggedIn = false;
        currentUsername = "";
    }
}
