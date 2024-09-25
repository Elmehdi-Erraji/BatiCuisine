package repository.implimentation;


import config.DBConnection;
import domain.entities.Labour;
import repository.Interfaces.LabourRepository;
import utils.Mappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LabourRepositoryImpl implements LabourRepository {
    private DBConnection dbConnection;
    private Connection connection = null;

    @Override
    public Labour save(Labour labour) {
        String sql = labour.getId() == null ?
                "INSERT INTO labour (name, tax_rate, hourlyrate, workhourscount, productivityrate, project_id) VALUES (?, ?, ?, ?, ?, ?)" :
                "UPDATE labour SET name = ?, tax_rate = ?, hourlyrate = ?, workhourscount = ?, productivityrate = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, labour.getName());
                    stmt.setDouble(2, labour.getTaxRate());
                    stmt.setDouble(3, labour.getHourlyRate());
                    stmt.setDouble(4, labour.getWorkHoursCount());
                    stmt.setDouble(5, labour.getProductivityRate());
                    stmt.setDouble(6, labour.getProject().getId());

                    if (labour.getId() != null) {
                        stmt.setInt(6, labour.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating mainDoeuvre failed, no rows affected.");
                    }

                    if (labour.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                labour.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("Creating mainDoeuvre failed, no ID obtained.");
                            }
                        }
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


        return labour;
    }

    @Override
    public Optional<Labour> findById(Integer id) {
        String sql = "SELECT * FROM labour WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToLabours(rs));
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
    public List<Labour> findAll() {
        List<Labour> labourList = new ArrayList<>();
        String sql = "SELECT * FROM labour";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        labourList.add(Mappers.mapResultSetToLabours(rs));
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


        return labourList;
    }

    @Override
    public void deleteById(Labour labour) {
        String sql = "DELETE FROM labour WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, labour.getId());
                    pstmt.executeUpdate();
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
