package repository.implimentation;

import domain.entities.Project;
import domain.entities.User;
import domain.enums.ProjectStatus;
import repository.Interfaces.ProjectRepository;
import service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final Connection connection;
    private final UserService userService;

    public ProjectRepositoryImpl(Connection connection, UserService userService) {
        this.connection = connection;
        this.userService = userService;
    }

    @Override
    public void addProject(Project project) {
        String query = "INSERT INTO projects (name, profit_margin, total_cost, project_status, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, project.getName());
            ps.setDouble(2, project.getProfitMargin());

            // Handle null value for total cost
            if (project.getTotalCost() == null) {
                ps.setNull(3, Types.DOUBLE); // Set total cost as NULL in the database
            } else {
                ps.setDouble(3, project.getTotalCost());
            }

            ps.setString(4, project.getProjectStatus().toString());
            ps.setInt(5, project.getUser().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Project getProjectById(int id) {
        String query = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProject(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                projects.add(mapRowToProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public void updateProject(Project project) {
        String query = "UPDATE projects SET name = ?, profit_margin = ?, total_cost = ?, project_status = ?, user_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, project.getName());
            ps.setDouble(2, project.getProfitMargin());
            ps.setDouble(3, project.getTotalCost());
            ps.setString(4, project.getProjectStatus().toString());
            ps.setInt(5, project.getUser().getId());
            ps.setInt(6, project.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProject(int id) {
        String query = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updated method to use the injected UserService
    private Project mapRowToProject(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double profitMargin = rs.getDouble("profit_margin");
        double totalCost = rs.getDouble("total_cost");
        ProjectStatus status = ProjectStatus.valueOf(rs.getString("project_status"));
        int userId = rs.getInt("user_id");

        // Use injected userService to get the User object
        User user = userService.getUserById(userId);
        return new Project(id, name, profitMargin, totalCost, status, user);
    }
}
