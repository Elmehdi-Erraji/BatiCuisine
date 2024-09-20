package repository.implimentation;

import config.dbConnection;
import domain.entities.Client;
import repository.Interfaces.CrudRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements CrudRepository<Client, Integer> {
    private final Connection connection;

    public ClientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Client> create(Client client) {
        try {
            String sql = "INSERT INTO clients (name, address, phone, isProfessional) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, client.getName());
            statement.setString(2, client.getAddress());
            statement.setString(3, client.getPhone());
            statement.setBoolean(4, client.isProfessional());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> findById(Integer id) {
        Client client = null;
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
                client = new Client(id, name, address, phone, isProfessional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(client);
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
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
                clients.add(new Client(id, name, address, phone, isProfessional));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Optional<Client> update(Client client) {
        try {
            String sql = "UPDATE clients SET name = ?, address = ?, phone = ?, isProfessional = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, client.getName());
            statement.setString(2, client.getAddress());
            statement.setString(3, client.getPhone());
            statement.setBoolean(4, client.isProfessional());
            statement.setInt(5, client.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return Optional.of(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void delete(Integer id) {
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
