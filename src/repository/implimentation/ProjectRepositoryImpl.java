package repository.implimentation;


import config.DBConnection;
import domain.entities.Project;
import repository.Interfaces.ProjectRepository;
import utils.Mappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {
    DBConnection dbConnection = null;
    Connection connection = null;

    @Override
    public Project save(Project project) {
        String sql = project.getId() == null ?
                "INSERT INTO projects (projectname, profit, totalcost, status, client_id, discount) VALUES (?, ?, ?, ?::projectstatus, ?, ?)" :
                "UPDATE projects SET projectname = ?, profit = ?, totalcost = ?, status = ?, client_id = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, project.getProjectName());
                    stmt.setDouble(2, project.getProfit());
                    stmt.setDouble(3, project.getTotalCost());
                    stmt.setString(4, project.getProjectStatus().name());
                    stmt.setInt(5, project.getClient().getId());
                    stmt.setDouble(6, project.getDiscount());

                    if (project.getId() != null) {
                        stmt.setInt(6, project.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating projet failed, no rows affected.");
                    }

                    if (project.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                project.setId(generatedKeys.getInt(1));
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
        return project;
    }

    @Override
    public Optional<Project> findById(Integer id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
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
    public List<Project> findAll() {
        List<Project> projectList = new ArrayList<>();
        String sql = "SELECT * FROM projects";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        projectList.add(Mappers.mapResultSetToProjet(rs));
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
        return projectList;
    }

    @Override
    public void delete(Project project) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, project.getId()); // Use project's ID here
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
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
