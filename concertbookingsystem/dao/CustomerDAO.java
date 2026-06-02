package com.mycompany.concertbookingsystem.dao;

import com.mycompany.concertbookingsystem.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDAO {
    private final DatabaseManager db;

    public CustomerDAO(DatabaseManager db) {
        this.db = db;
    }

    public boolean registerCustomer(String username, String password) {
        if (isUsernameTaken(username)) return false;
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ CustomerDAO.registerCustomer: " + e.getMessage());
            return false;
        }
    }

    public boolean isUsernameTaken(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("❌ CustomerDAO.isUsernameTaken: " + e.getMessage());
            return true; // treat errors as taken to be safe
        }
    }

    public List<Map<String, Object>> getAllCustomers() {
        String sql = "SELECT u.id, u.username, COALESCE(COUNT(b.id),0) AS total_bookings FROM users u LEFT JOIN bookings b ON b.customer_name = u.username GROUP BY u.id, u.username";
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= cols; i++) row.put(md.getColumnLabel(i), rs.getObject(i));
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println("❌ CustomerDAO.getAllCustomers: " + e.getMessage());
        }
        return rows;
    }
}
