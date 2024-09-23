package repository.implimentation;


import config.DBConnection;
import domain.entities.Material;
import repository.Interfaces.MaterialsRepository;
import utils.Mappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialsRepositoryImpl implements MaterialsRepository {
    private DBConnection dbConnection;
    private Connection connection = null;

    @Override
    public Material save(Material material) {
        String sql = material.getId() == null ?
                "INSERT INTO materials (name, tax_rate, unitcost, quantity, transportcost, qualitycoefficient, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)" :
                "UPDATE materials SET name = ?, tax_rate = ?, unitcost = ?, quantity = ?, transportcost = ?, qualitycoefficient = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, material.getName());
                    stmt.setDouble(2, material.getTaxRate());
                    stmt.setDouble(3, material.getUnitCost());
                    stmt.setDouble(4, material.getQuantity());
                    stmt.setDouble(5, material.getTransportCost());
                    stmt.setDouble(6, material.getQualityCoefficient());
                    stmt.setDouble(7, material.getProject().getId());


                    if (material.getId() != null) {
                        stmt.setInt(7, material.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating materiau failed, no rows affected.");
                    }

                    if (material.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                material.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("Creating materiau failed, no ID obtained.");
                            }
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }  finally {
                    if (dbConnection != null) {
                        dbConnection.closeConnection();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return material;
    }

    @Override
    public Optional<Material> findById(Integer id) {
        String sql = "SELECT * FROM materials WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToMaterials(rs));
                    }
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

        return Optional.empty();
    }

    @Override
    public List<Material> findAll() {
        List<Material> materialList = new ArrayList<>();
        String sql = "SELECT * FROM materials";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        materialList.add(Mappers.mapResultSetToMaterials(rs));
                    }
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



        return materialList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM materials WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
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

