package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    private static Connection connection = null;

    private dbConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (dbConnection.class) {
                if (connection == null) {
                    try {
                        // Initialize the database connection
                        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/batiCuisine", "batiCuisine", "");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }
}
