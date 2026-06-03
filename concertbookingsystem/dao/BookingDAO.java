package com.mycompany.concertbookingsystem.dao;

import com.mycompany.concertbookingsystem.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDAO {
    private final DatabaseManager db;

    public BookingDAO(DatabaseManager db) {
        this.db = db;
    }

    public boolean createBooking(String customerName, int concertId, String concertName, String seatType, int quantity, double unitPrice) {
        String insertSql = "INSERT INTO bookings (customer_name, concert_id, concert_name, seat_type, quantity, ticket_price, status) VALUES (?, ?, ?, ?, ?, ?, 'BOOKED')";
        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                ins.setString(1, customerName);
                ins.setInt(2, concertId);
                ins.setString(3, concertName);
                ins.setString(4, seatType);
                ins.setInt(5, quantity);
                ins.setDouble(6, unitPrice * quantity);
                ins.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ BookingDAO.createBooking: " + e.getMessage());
            return false;
        }
    }

    public Map<String, Object> getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
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
            System.out.println("❌ BookingDAO.getBookingById: " + e.getMessage());
        }
        return null;
    }

    public boolean cancelBooking(int bookingId) {
        // Retrieve booking to understand seat type and concert id
        Map<String, Object> booking = getBookingById(bookingId);
        if (booking == null) return false;
        int concertId = ((Number) booking.get("concert_id")).intValue();
        String seatType = String.valueOf(booking.get("seat_type"));
        int quantity = ((Number) booking.get("quantity")).intValue();

        String restoreSql = seatType.equalsIgnoreCase("VIP") ?
                "UPDATE concerts SET vip_seats = vip_seats + ? WHERE id = ?" :
                "UPDATE concerts SET basic_seats = basic_seats + ? WHERE id = ?";

        String updateBookingSql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";

        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement restore = conn.prepareStatement(restoreSql)) {
                restore.setInt(1, quantity);
                restore.setInt(2, concertId);
                restore.executeUpdate();
            }
            try (PreparedStatement upd = conn.prepareStatement(updateBookingSql)) {
                upd.setInt(1, bookingId);
                upd.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ BookingDAO.cancelBooking: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> getAllBookings() {
        String sql = "SELECT b.*, c.concert_date AS concert_date FROM bookings b LEFT JOIN concerts c ON b.concert_id = c.id";
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
            System.out.println("❌ BookingDAO.getAllBookings: " + e.getMessage());
        }
        return rows;
    }

    public List<Map<String, Object>> getBookingsForEventLike(String eventNamePrefix) {
        String sql = "SELECT * FROM bookings WHERE concert_name LIKE ?";
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, eventNamePrefix + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= cols; i++) row.put(md.getColumnLabel(i), rs.getObject(i));
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ BookingDAO.getBookingsForEventLike: " + e.getMessage());
        }
        return rows;
    }

    /**
     * Update an existing booking's customer name, quantity, and seat type.
     * Uses PreparedStatement to safely prevent SQL injection.
     */
    public boolean updateBooking(int bookingId, String customerName, String seatType, int quantity) {
        String sql = "UPDATE bookings SET customer_name = ?, seat_type = ?, quantity = ? WHERE id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, seatType);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, bookingId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("❌ BookingDAO.updateBooking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a booking by ID.
     * Uses PreparedStatement to safely prevent SQL injection.
     */
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("❌ BookingDAO.deleteBooking: " + e.getMessage());
            return false;
        }
    }

}
