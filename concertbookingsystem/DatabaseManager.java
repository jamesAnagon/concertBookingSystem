package com.mycompany.concertbookingsystem;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/concert_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default XAMPP password is empty

    // Helper to get database connection
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREATE (Accepts ANY Ticket type using POLYMORPHISM)
    public void createBooking(Ticket ticket) {
        String sql = "INSERT INTO bookings (customer_name, concert_name, ticket_price, ticket_type, special_perk) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ticket.getCustomerName());
            pstmt.setString(2, ticket.getConcertName());
            pstmt.setDouble(3, ticket.calculateFinalPrice()); // Polymorphic method call
            pstmt.setString(4, ticket.getTicketType());       // Polymorphic method call
            pstmt.setString(5, ticket.getSpecialPerk());      // Polymorphic method call
            
            pstmt.executeUpdate();
            System.out.println("🎉 Booking saved to DB successfully for " + ticket.getCustomerName());
        } catch (SQLException e) {
            System.out.println("❌ Create Error: " + e.getMessage());
        }
    }

    // READ
    public void readBookings() {
        String sql = "SELECT * FROM bookings";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- CURRENT CONCERT BOOKINGS ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Concert: %s | Price: $%.2f | Type: %s | Perk: %s\n",
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("concert_name"),
                        rs.getDouble("ticket_price"),
                        rs.getString("ticket_type"),
                        rs.getString("special_perk"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Read Error: " + e.getMessage());
        }
    }

    // UPDATE
    public void updateCustomerName(int id, String newName) {
        String sql = "UPDATE bookings SET customer_name = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("🔄 Booking ID " + id + " updated successfully!");
            } else {
                System.out.println("⚠️ Booking ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Update Error: " + e.getMessage());
        }
    }

    // DELETE
    public void deleteBooking(int id) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("🗑️ Booking ID " + id + " deleted from database.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Delete Error: " + e.getMessage());
        }
    }


// Add these two methods inside your existing DatabaseManager class:

public boolean registerUser(String username, String password) {
    String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/concert_db", "root", "");
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.setString(2, password); // For production, use password hashing!
        pstmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.out.println("❌ Registration Error: " + e.getMessage());
        return false;
    }
}

public boolean loginUser(String username, String password) {
    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/concert_db", "root", "");
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        return rs.next(); // Returns true if a match is found
    } catch (SQLException e) {
        System.out.println("❌ Login Error: " + e.getMessage());
        return false;
    }
}

}
