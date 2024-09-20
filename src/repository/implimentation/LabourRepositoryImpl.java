package repositories.MainDoeuvre;

import Config.DBConnection;
import Entities.MainDoeuvre;
import Enums.TypeComposant;
import Utils.Mappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainDoeuvreRepositoryImpl implements MainDoeuvreRepository {
    private DBConnection dbConnection;
    private Connection connection = null;

    @Override
    public MainDoeuvre save(MainDoeuvre mainDoeuvre) {
        String sql = mainDoeuvre.getId() == null ?
                "INSERT INTO MainDœuvre (name, taxRate, hourlyRate, workHoursCount, productivityRate, project_id) VALUES (?, ?, ?, ?, ?, ?)" :
                "UPDATE MainDœuvre SET name = ?, taxRate = ?, hourlyRate = ?, workHoursCount = ?, productivityRate = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, mainDoeuvre.getName());
                    stmt.setDouble(2, mainDoeuvre.getTaxRate());
                    stmt.setDouble(3, mainDoeuvre.getHourlyRate());
                    stmt.setDouble(4, mainDoeuvre.getWorkHoursCount());
                    stmt.setDouble(5, mainDoeuvre.getProductivityRate());
                    stmt.setDouble(6, mainDoeuvre.getProjet().getId());

                    if (mainDoeuvre.getId() != null) {
                        stmt.setInt(6, mainDoeuvre.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating mainDoeuvre failed, no rows affected.");
                    }

                    if (mainDoeuvre.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                mainDoeuvre.setId(generatedKeys.getInt(1));
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


        return mainDoeuvre;
    }

    @Override
    public Optional<MainDoeuvre> findById(Integer id) {
        String sql = "SELECT * FROM MainDœuvre WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToMainDœuvre(rs));
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
    public List<MainDoeuvre> findAll() {
        List<MainDoeuvre> mainDoeuvreList = new ArrayList<>();
        String sql = "SELECT * FROM MainDœuvre";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        mainDoeuvreList.add(Mappers.mapResultSetToMainDœuvre(rs));
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


        return mainDoeuvreList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM MainDœuvre WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
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
