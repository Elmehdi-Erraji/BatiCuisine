package repository.implimentation;

import config.dbConnection;
import domain.entities.Material;
import domain.enums.ComponentType;
import repository.Interfaces.MaterialsRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialsRepositoryImpl implements MaterialsRepository {
    private final Connection connection;

    public MaterialsRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Material save(Material material) {
        String sql = material.getId() == null ?
                "INSERT INTO material (name, taxRate, unitCost, quantity, transportCost, qualityCoefficient) VALUES (?, ?, ?, ?, ?, ?)" :
                "UPDATE material SET name = ?, taxRate = ?, unitCost = ?, quantity = ?, transportCost = ?, qualityCoefficient = ? WHERE id = ?";


                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, material.getName());
                    stmt.setDouble(2, material.getTaxRate());
                    stmt.setDouble(3, material.getUnitCost());
                    stmt.setDouble(4, material.getQuantity());
                    stmt.setDouble(5, material.getTransportCost());
                    stmt.setDouble(6, material.getQualityCoefficient());

                    if (material.getId() != null) {
                        stmt.setInt(7, material.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating material failed, no rows affected.");
                    }

                    if (material.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                material.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("Creating material failed, no ID obtained.");
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        return material;
    }

    @Override
    public Optional<Material> findById(Integer id) {
        String sql = "SELECT * FROM material WHERE id = ?";

                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(mapResultSetToMaterials(rs));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        return Optional.empty();
    }

    @Override
    public List<Material> findAll() {
        List<Material> materialsList = new ArrayList<>();
        String sql = "SELECT * FROM material";

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        materialsList.add(mapResultSetToMaterials(rs));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        return materialsList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM material WHERE id = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    private Material mapResultSetToMaterials(ResultSet rs) throws SQLException {
        return new Material(
                rs.getString("name"),
                rs.getDouble("taxRate"),
                rs.getInt("id"),
                ComponentType.MATERIAL,
                rs.getDouble("unitCost"),
                rs.getDouble("quantity"),
                rs.getDouble("transportCost"),
                rs.getDouble("qualityCoefficient")
        );
    }
}


