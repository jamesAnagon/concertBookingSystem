package com.mycompany.concertbookingsystem;

import javax.swing.SwingUtilities;

public class ConcertBookingSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Updated launcher to kick-off from the stylized landing home screen
            new HomePage().setVisible(true);
        });
    }
}
