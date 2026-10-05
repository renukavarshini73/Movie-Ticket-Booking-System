package com.movieticket.dao;

import com.movieticket.model.Customer;
import com.movieticket.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for CUSTOMER table.
 */
public class CustomerDAO {

    public Customer findByPhone(String phone) throws SQLException {
        String sql = "SELECT customer_id, name, phone, email FROM customer WHERE phone = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Inserts or retrieves an existing Customer within a JDBC transaction.
     */
    public int insertOrGetCustomer(Customer customer, Connection conn) throws SQLException {
        // Check if phone number already registered
        String searchSql = "SELECT customer_id FROM customer WHERE phone = ?";
        try (PreparedStatement ps = conn.prepareStatement(searchSql)) {
            ps.setString(1, customer.getPhone());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("customer_id");
                }
            }
        }

        // Insert new customer using Oracle IDENTITY column generated key
        String insertSql = "INSERT INTO customer (name, phone, email) VALUES (?, ?, ?)";
        String[] generatedColumns = {"CUSTOMER_ID"};
        try (PreparedStatement ps = conn.prepareStatement(insertSql, generatedColumns)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Creating customer failed, no ID obtained.");
    }
}
