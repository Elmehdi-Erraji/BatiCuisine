package repository.implimentation;

import config.dbConnection;
import domain.entities.Labour;
import domain.enums.ComponentType;
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
    public Labour save(Labour labour) {
        String sql = labour.getId() == null ?
                "INSERT INTO labours (name, taxrate, hourlyrate, workhourscount, productivityrate) VALUES (?, ?, ?, ?, ?)" :
                "UPDATE labours SET name = ?, taxrate = ?, hourlyrate = ?, workhourscount = ?, productivityrate = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, labour.getName());
            stmt.setDouble(2, labour.getTaxRate());
            stmt.setDouble(3, labour.getHourlyRate());
            stmt.setDouble(4, labour.getWorkHoursCount());
            stmt.setDouble(5, labour.getProductivityRate());

            if (labour.getId() != null) {
                stmt.setInt(6, labour.getId());
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating labour failed, no rows affected.");
            }

            if (labour.getId() == null) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        labour.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating labour failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return labour;
    }

    @Override
    public Optional<Labour> findById(Integer id) {
        String sql = "SELECT * FROM labours WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToLabour(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Labour> findAll() {
        List<Labour> labourList = new ArrayList<>();
        String sql = "SELECT * FROM labours";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                labourList.add(mapResultSetToLabour(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return labourList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM labours WHERE id = ?";  // Updated table name to 'labours'

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Labour mapResultSetToLabour(ResultSet rs) throws SQLException {
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


   
