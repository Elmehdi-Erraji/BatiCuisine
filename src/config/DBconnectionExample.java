package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnectionExample {

    private static DBconnectionExample instance;
    private final Connection connection;

    private static final String URL = "";//link to your db server
    private static final String USER = "";//db username
    private static final String PASSWORD = "";//db password

    private DBconnectionExample() {
        Connection tempConnection = null;
        try {
            tempConnection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = tempConnection;
    }

    public static DBconnectionExample getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBconnectionExample();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBconnectionExample();
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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
