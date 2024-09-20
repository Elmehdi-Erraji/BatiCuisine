package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    private static dbConnection instance;
    private final Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/batiCuisine";
    private static final String USER = "batiCuisine";
    private static final String PASSWORD = "";

    private dbConnection() {
        Connection tempConnection = null;
        try {
            tempConnection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = tempConnection;
    }

    public static dbConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new dbConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new dbConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
