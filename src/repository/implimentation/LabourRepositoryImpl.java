package repository.implimentation;

import config.dbConnection;
import domain.entities.Labour;
import domain.enums.ComponentType;
import domain.entities.Labour;
import repository.Interfaces.LaborRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LabourRepositoryImpl implements LaborRepository {
    private final Connection connection;

    public LabourRepositoryImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Labour save(Labour labor) {
        String sql = labor.getId() == null ?
                "INSERT INTO labours (name, taxRate, hourlyRate, workHoursCount, productivityRate) VALUES (?, ?, ?, ?, ?)" :
                "UPDATE labours SET name = ?, taxRate = ?, hourlyRate = ?, workHoursCount = ?, productivityRate = ? WHERE id = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, labor.getName());
                    stmt.setDouble(2, labor.getTaxRate());
                    stmt.setDouble(3, labor.getHourlyRate());
                    stmt.setDouble(4, labor.getWorkHoursCount());
                    stmt.setDouble(5, labor.getProductivityRate());

                    if (labor.getId() != null) {
                        stmt.setInt(6, labor.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating labor failed, no rows affected.");
                    }

                    if (labor.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                labor.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("Creating labor failed, no ID obtained.");
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        return labor;
    }

    @Override
    public Optional<Labour> findById(Integer id) {
        String sql = "SELECT * FROM labours WHERE id = ?";

                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(mapResultSetToLabor(rs));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        return Optional.empty();
    }

    @Override
    public List<Labour> findAll() {
        List<Labour> laborList = new ArrayList<>();
        String sql = "SELECT * FROM labours";

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        laborList.add(mapResultSetToLabor(rs));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        return laborList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM labours WHERE id = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    private Labour mapResultSetToLabor(ResultSet rs) throws SQLException {
        return new Labour(
                rs.getString("name"),
                rs.getDouble("taxRate"),
                rs.getInt("id"),
                ComponentType.LABOR,
                rs.getDouble("hourlyRate"),
                rs.getDouble("workHoursCount"),
                rs.getDouble("productivityRate")
        );
    }
}


