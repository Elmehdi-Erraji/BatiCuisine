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
    public Material save(Material materiau) {
        String sql = materiau.getId() == null ?
                "INSERT INTO Materiaux (name, taxRate, unitCost, quantity, transportCost, qualityCoefficient, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)" :
                "UPDATE Materiaux SET name = ?, taxRate = ?, unitCost = ?, quantity = ?, transportCost = ?, qualityCoefficient = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, materiau.getName());
                    stmt.setDouble(2, materiau.getTaxRate());
                    stmt.setDouble(3, materiau.getUnitCost());
                    stmt.setDouble(4, materiau.getQuantity());
                    stmt.setDouble(5, materiau.getTransportCost());
                    stmt.setDouble(6, materiau.getQualityCoefficient());
                    stmt.setDouble(7, materiau.getProject().getId());


                    if (materiau.getId() != null) {
                        stmt.setInt(7, materiau.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating materiau failed, no rows affected.");
                    }

                    if (materiau.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                materiau.setId(generatedKeys.getInt(1));
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

        return materiau;
    }

    @Override
    public Optional<Material> findById(Integer id) {
        String sql = "SELECT * FROM Materiaux WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToMateriaux(rs));
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
        String sql = "SELECT * FROM Materiaux";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        materialList.add(Mappers.mapResultSetToMateriaux(rs));
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
        String sql = "DELETE FROM Materiaux WHERE id = ?";

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

