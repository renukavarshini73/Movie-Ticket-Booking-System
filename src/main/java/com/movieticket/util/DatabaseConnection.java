package com.movieticket.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing Oracle JDBC Database Connections.
 * Security Note: Database credentials are read dynamically from config.properties 
 * or environment variables (DB_URL, DB_USER, DB_PASSWORD) to prevent hardcoding.
 */
public class DatabaseConnection {

    private static String url;
    private static String user;
    private static String password;

    static {
        loadConfiguration();
    }

    private static void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
                url = props.getProperty("db.url");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");
            }
        } catch (Exception e) {
            System.err.println("[DatabaseConnection] Failed to load config.properties: " + e.getMessage());
        }

        // Fallback to Environment Variables if properties not set
        if (url == null || url.trim().isEmpty()) {
            url = System.getenv("DB_URL");
        }
        if (user == null || user.trim().isEmpty()) {
            user = System.getenv("DB_USER");
        }
        if (password == null || password.trim().isEmpty()) {
            password = System.getenv("DB_PASSWORD");
        }

        try {
            // Explicitly register Oracle JDBC Driver
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseConnection] Oracle JDBC Driver not found: " + e.getMessage());
        }
    }

    /**
     * Creates and returns a new JDBC connection to Oracle Database.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (url == null || user == null || password == null) {
            throw new SQLException("Database credentials not initialized properly.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Utility method to test database connectivity.
     */
    public static boolean testConnection() {
        System.out.println("Testing connection to Oracle Database at: " + url + " as User: " + user);
        try (Connection conn = getConnection()) {
            boolean valid = conn != null && !conn.isClosed();
            if (valid) {
                System.out.println("SUCCESS: Connection to Oracle Database established successfully!");
                System.out.println("Oracle DB Product Name: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Oracle DB Product Version: " + conn.getMetaData().getDatabaseProductVersion());
            }
            return valid;
        } catch (SQLException e) {
            System.err.println("FAILED: Unable to connect to Oracle Database.");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        testConnection();
    }
}
