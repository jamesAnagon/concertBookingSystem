package com.mycompany.concertbookingsystem;

import javax.swing.*;

public class NavigationManager {
    public void showLoginFrame(JFrame currentFrame) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        new LoginFrame().setVisible(true);
    }

    public void showDashboardFrame(JFrame currentFrame) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        new MainDashboardFrame().setVisible(true);
    }

    public void showSeatSelectionFrame(MainDashboardFrame dashboardFrame, DatabaseManager databaseManager, String eventName) {
        dashboardFrame.setVisible(false);
        SeatSelectionFrame seatSelectionFrame = new SeatSelectionFrame(
                dashboardFrame,
                databaseManager,
                eventName,
                AppTheme.PRIMARY_BLUE,
                AppTheme.WHITE
        );
        seatSelectionFrame.setVisible(true);
    }
}
