package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    private static Connection connection = null;

    private dbConnection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (dbConnection.class) {
                if (connection == null) {
                    try {
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
