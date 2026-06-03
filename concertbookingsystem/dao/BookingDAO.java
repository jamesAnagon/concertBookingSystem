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
        // Insert into bookings table matching the actual schema:
        // customer_name, concert_name, ticket_price, ticket_type
        String ticketType = seatType; // "VIP" or "BASIC"
        double totalPrice = unitPrice * quantity;

        String insertSql = "INSERT INTO bookings (customer_name, concert_name, ticket_price, ticket_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                ins.setString(1, customerName);
                ins.setString(2, concertName);
                ins.setDouble(3, totalPrice);
                ins.setString(4, ticketType);
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
        // Retrieve booking to understand seat type and concert reference
        Map<String, Object> booking = getBookingById(bookingId);
        if (booking == null) return false;
        String seatType = String.valueOf(booking.get("seat_type"));
        int quantity = ((Number) booking.get("quantity")).intValue();
        String concertName = String.valueOf(booking.get("concert_name"));

        // Try to resolve concert id by name; if found, prefer updating by id, otherwise by name.
        int concertId = -1;
        try {
            com.mycompany.concertbookingsystem.dao.ConcertDAO concertDAO = new com.mycompany.concertbookingsystem.dao.ConcertDAO(db);
            Map<String, Object> concert = concertDAO.getConcertByTitle(concertName);
            if (concert != null && concert.get("id") instanceof Number) {
                concertId = ((Number) concert.get("id")).intValue();
            }
        } catch (Exception ignore) {}

        String restoreSql;
        if (seatType.equalsIgnoreCase("VIP")) {
            restoreSql = concertId > 0 ?
                    "UPDATE concerts SET vip_seats = vip_seats + ? WHERE id = ?" :
                    "UPDATE concerts SET vip_seats = vip_seats + ? WHERE concert_name = ?";
        } else {
            restoreSql = concertId > 0 ?
                    "UPDATE concerts SET basic_seats = basic_seats + ? WHERE id = ?" :
                    "UPDATE concerts SET basic_seats = basic_seats + ? WHERE concert_name = ?";
        }

        String updateBookingSql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";

        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement restore = conn.prepareStatement(restoreSql)) {
                restore.setInt(1, quantity);
                if (concertId > 0) restore.setInt(2, concertId);
                else restore.setString(2, concertName);
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
        // Query only the bookings table. The bookings schema in this project stores concert_name
        // rather than a concert_id, so joining on a non-existent column caused errors.
        String sql = "SELECT * FROM bookings";
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
     * Check if a specific concert_name entry (including seat) is already booked.
     */
    public boolean isSeatBooked(String fullConcertName) {
        String sql = "SELECT 1 FROM bookings WHERE concert_name = ? LIMIT 1";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullConcertName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("❌ BookingDAO.isSeatBooked: " + e.getMessage());
            return false; // treat errors as not booked to avoid blocking UI; caller may re-check
        }
    }

    /**
     * Update an existing booking's customer name and ticket type.
     * Uses PreparedStatement to safely prevent SQL injection.
     */
    public boolean updateBooking(int bookingId, String customerName, String ticketType) {
        String sql = "UPDATE bookings SET customer_name = ?, ticket_type = ? WHERE id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, ticketType);
            pstmt.setInt(3, bookingId);
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
