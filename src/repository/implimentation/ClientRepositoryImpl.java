package repository.implimentation;


import config.DBConnection;
import domain.entities.Client;
import repository.Interfaces.ClientRepository;

import java.sql.*;
import java.util.*;

import utils.Mappers;

public class ClientRepositoryImpl implements ClientRepository {
    DBConnection dbConnection = null;
    Connection connection = null;

    @Override
    public Client save(Client client) {
        String sql = client.getId() == null ?
                "INSERT INTO Clients (name, address, phone, isProfessional) VALUES (?, ?, ?, ?)" :
                "UPDATE Clients SET name = ?, address = ?, phone = ?, isProfessional = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, client.getName());
                    stmt.setString(2, client.getAddress());
                    stmt.setString(3, client.getPhone());
                    stmt.setBoolean(4, client.getProfessional());

                    if (client.getId() != null) {
                        stmt.setInt(5, client.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating client failed, no rows affected.");
                    }

                    if (client.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                client.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("Creating client failed, no ID obtained.");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
        return client;
    }

    @Override
    public Optional<Client> findById(Integer id) {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToClient(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
        return Optional.empty();
    }


    @Override
    public Optional<Client> findByName(String name) {
        String sql = "SELECT * FROM clients WHERE name = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, name);  // Set the name as a parameter
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToClient(rs));  // Map the result set to a Client object
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
        return Optional.empty();
    }


    @Override
    public List<Client> findAll() {
        List<Client> clientList = new ArrayList<>();
        String sql = "SELECT * FROM clients";


        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        clientList.add(Mappers.mapResultSetToClient(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
        return clientList;
    }

    @Override
    public void deleteById(Client client) {
        String sql = "DELETE FROM clients WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, client.getId());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
    }

    @Override
    public List<Client> findByProfessional(Boolean isProfessional) {
        List<Client> clientList = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE isProfessional = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setBoolean(1, isProfessional);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        clientList.add(Mappers.mapResultSetToClient(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
        return clientList;
    }


    public int getRejectedQuotesCount(Integer clientId) {
        String sql = "SELECT COUNT(q.id) AS rejected_count " +
                "FROM clients c " +
                "JOIN projects p ON c.id = p.client_id " +
                "JOIN quotes q ON p.id = q.project_id " +
                "WHERE c.id = ? AND q.accepted = FALSE";

        int rejectedCount = 0;

        try {
            dbConnection = DBConnection.getInstance();
            Connection connection = dbConnection.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, clientId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        rejectedCount = rs.getInt("rejected_count");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }

        return rejectedCount;
    }


    public void updateClientStatus(Integer clientId, Boolean status) {
        String sql = "UPDATE clients SET status = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            Connection connection = dbConnection.getConnection();

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBoolean(1, status);
                stmt.setInt(2, clientId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (dbConnection != null) {
                dbConnection.closeConnection();
            }
        }
    }

}