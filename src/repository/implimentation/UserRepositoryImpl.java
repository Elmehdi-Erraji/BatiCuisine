package repository.implimentation;

import domain.entities.User;
import repository.Interfaces.UserRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection;

    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(User user) {
        try {
            String sql = "INSERT INTO clients (name, address, phone, isProfessional) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getPhone());
            statement.setBoolean(4, user.isProfessional());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(int id) {
        User user = null;
        try {
            String sql = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                boolean isProfessional = resultSet.getBoolean("isProfessional");
                user = new User(id, name, address, phone, isProfessional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM clients";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                boolean isProfessional = resultSet.getBoolean("isProfessional");
                users.add(new User(id, name, address, phone, isProfessional));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void update(User user) {
        try {
            String sql = "UPDATE clients SET name = ?, address = ?, phone = ?, isProfessional = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getPhone());
            statement.setBoolean(4, user.isProfessional());
            statement.setInt(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM clients WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
