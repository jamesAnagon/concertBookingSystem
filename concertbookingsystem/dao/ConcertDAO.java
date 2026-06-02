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

    public Map<String, Object> getConcertByTitle(String title) {
        String sql = "SELECT * FROM concerts WHERE title = ? LIMIT 1";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
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

    public boolean addConcert(String title, String artist, String venue, Date date, double price, int vipSeats, int basicSeats) {
        String sql = "INSERT INTO concerts (title, artist, venue, date, price, vip_seats, basic_seats) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            pstmt.setString(3, venue);
            pstmt.setDate(4, date);
            pstmt.setDouble(5, price);
            pstmt.setInt(6, vipSeats);
            pstmt.setInt(7, basicSeats);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ ConcertDAO.addConcert: " + e.getMessage());
            return false;
        }
    }

    public boolean updateConcert(int id, String title, String artist, String venue, Date date, double price, int vipSeats, int basicSeats) {
        String sql = "UPDATE concerts SET title=?, artist=?, venue=?, date=?, price=?, vip_seats=?, basic_seats=? WHERE id=?";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            pstmt.setString(3, venue);
            pstmt.setDate(4, date);
            pstmt.setDouble(5, price);
            pstmt.setInt(6, vipSeats);
            pstmt.setInt(7, basicSeats);
            pstmt.setInt(8, id);
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
