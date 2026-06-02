package com.mycompany.concertbookingsystem.dao;

import com.mycompany.concertbookingsystem.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ConcertDAO {
    private final DatabaseManager db;

    public ConcertDAO(DatabaseManager db) {
        this.db = db;
    }

    public List<Map<String, Object>> getAllConcerts() {
        String sql = "SELECT * FROM concerts";
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
            System.out.println("❌ ConcertDAO.getAllConcerts: " + e.getMessage());
        }
        return rows;
    }

    public Map<String, Object> getConcertByTitle(String concertName) {
        String sql = "SELECT * FROM concerts WHERE concert_name = ? LIMIT 1";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, concertName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ResultSetMetaData md = rs.getMetaData();
                    int cols = md.getColumnCount();
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= cols; i++) row.put(md.getColumnLabel(i), rs.getObject(i));
                    return row;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ ConcertDAO.getConcertByTitle: " + e.getMessage());
        }
        return null;
    }

    public boolean addConcert(String concertName, Date concertDate, double ticketPrice, double regularPrice, double vipPrice) {
        String sql = "INSERT INTO concerts (concert_name, concert_date, ticket_price, regular_price, vip_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, concertName);
            pstmt.setDate(2, concertDate);
            pstmt.setDouble(3, ticketPrice);
            pstmt.setDouble(4, regularPrice);
            pstmt.setDouble(5, vipPrice);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ ConcertDAO.addConcert: " + e.getMessage());
            return false;
        }
    }

    public boolean updateConcert(int id, String concertName, Date concertDate, double ticketPrice, double regularPrice, double vipPrice) {
        String sql = "UPDATE concerts SET concert_name=?, concert_date=?, ticket_price=?, regular_price=?, vip_price=? WHERE id=?";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, concertName);
            pstmt.setDate(2, concertDate);
            pstmt.setDouble(3, ticketPrice);
            pstmt.setDouble(4, regularPrice);
            pstmt.setDouble(5, vipPrice);
            pstmt.setInt(6, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ ConcertDAO.updateConcert: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteConcert(int id) {
        String sql = "DELETE FROM concerts WHERE id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ ConcertDAO.deleteConcert: " + e.getMessage());
            return false;
        }
    }
}
