package repository.implimentation;

import config.dbConnection;
import domain.enums.ProjectStatus;
import domain.entities.Project;
import repository.Interfaces.ProjectRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final Connection connection;

    public ProjectRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Project save(Project project) {
        String sql = project.getId() == null ?
                "INSERT INTO projects (projectName, profit, totalCost, status, client_id) VALUES (?, ?, ?, ?::projectstatus, ?)" :
                "UPDATE projects SET projectName = ?, profit = ?, totalCost = ?, status = ?, client_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, project.getName());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setDouble(3, project.getTotalCost());
            stmt.setString(4, project.getProjectStatus().name());
            stmt.setInt(5, project.getClient().getId());

            if (project.getId() != null) {
                stmt.setInt(6, project.getId());
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return null; // No project created/updated
            }

            if (project.getId() == null) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating project failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }



    @Override
    public Optional<Project> findById(Integer id) {
        String sql = "SELECT * FROM Projects WHERE id = ?";
        Project Project = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Project = mapResultSetToProject(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(Project);
    }

    @Override
    public List<Project> findAll() {
        List<Project> ProjectList = new ArrayList<>();
        String sql = "SELECT * FROM Projects";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProjectList.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ProjectList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Projects WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Project mapResultSetToProject(ResultSet rs) throws SQLException {
        return new Project(
                rs.getInt("id"),
                rs.getString("projectName"),
                rs.getDouble("profit"),
                rs.getDouble("totalCost"),
                ProjectStatus.valueOf(rs.getString("status"))
        );
    }
}
