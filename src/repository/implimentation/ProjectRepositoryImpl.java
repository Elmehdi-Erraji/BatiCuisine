package repositories.Projet;

import Config.DBConnection;
import Entities.Projet;
import Utils.Mappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjetRepositoryImpl implements ProjetRepository {
    DBConnection dbConnection = null;
    Connection connection = null;

    @Override
    public Projet save(Projet projet) {
        String sql = projet.getId() == null ?
                "INSERT INTO Projets (projectName, profit, totalCost, status, client_id, discount) VALUES (?, ?, ?, ?::projectstatus, ?, ?)" :
                "UPDATE Projets SET projectName = ?, profit = ?, totalCost = ?, status = ?, client_id = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, projet.getProjectName());
                    stmt.setDouble(2, projet.getProfit());
                    stmt.setDouble(3, projet.getTotalCost());
                    stmt.setString(4, projet.getProjectStatus().name());
                    stmt.setInt(5, projet.getClient().getId());
                    stmt.setDouble(6, projet.getDiscount());

                    if (projet.getId() != null) {
                        stmt.setInt(6, projet.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating projet failed, no rows affected.");
                    }

                    if (projet.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                projet.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("Creating projet failed, no ID obtained.");
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
        return projet;
    }

    @Override
    public Optional<Projet> findById(Integer id) {
        String sql = "SELECT * FROM Projets WHERE id = ?";
        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToProjet(rs));
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
    public List<Projet> findAll() {
        List<Projet> projetList = new ArrayList<>();
        String sql = "SELECT * FROM Projets";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        projetList.add(Mappers.mapResultSetToProjet(rs));
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
        return projetList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Projets WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
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

}
