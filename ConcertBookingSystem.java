package com.mycompany.concertbookingsystem;

import javax.swing.SwingUtilities;

public class ConcertBookingSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NavigationManager().showLoginFrame(null);
        });
    }
}
