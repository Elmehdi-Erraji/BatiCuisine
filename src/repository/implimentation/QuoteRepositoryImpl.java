package repositories.Devis;


import Config.DBConnection;
import Entities.Client;
import Entities.Devis;
import Entities.Projet;
import Enums.EtatProject;
import Utils.Mappers;
import repositories.Projet.ProjetRepositoryImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DevisRepositoryImpl implements DevisRepository {
    private DBConnection dbConnection;
    private Connection connection = null;

    @Override
    public Devis save(Devis devis) {
        String sql = devis.getId() == null ? "INSERT INTO Devis (estimatedPrice, issueDate, validityDate, accepted, project_id) VALUES (?, ?, ?, ?, ?)" : "UPDATE Devis SET estimatedPrice = ?, issueDate = ?, validityDate = ?, accepted = ?, project_id = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setDouble(1, devis.getEstimatedPrice());
                    stmt.setDate(2, Date.valueOf(devis.getIssueDate()));
                    stmt.setDate(3, Date.valueOf(devis.getValidityDate()));
                    stmt.setBoolean(4, devis.getAccepted());
                    stmt.setInt(5, devis.getProjet().getId());

                    if (devis.getId() != null) {
                        stmt.setInt(6, devis.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating devis failed, no rows affected.");
                    }

                    if (devis.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                devis.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("Creating devis failed, no ID obtained.");
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


        return devis;
    }

    @Override
    public Optional<Devis> findById(Integer id) {
        String sql = "SELECT * FROM Devis WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToDevis(rs));
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
    public List<Devis> findAll() {
        List<Devis> devisList = new ArrayList<>();
        String sql = "SELECT * FROM Devis";
        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        devisList.add(Mappers.mapResultSetToDevis(rs));
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


        return devisList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Devis WHERE id = ?";
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

    @Override
    public List<Devis> findDevisJoinProjectsById(Client client) {
        List<Devis> devisList = new ArrayList<>();
        String sql = "Select * FROM Devis d JOIN projets p ON d.project_id = p.id WHERE p.client_id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, client.getId());
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        devisList.add(Mappers.mapResultSetToDevisAndPorject(rs));
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


        return devisList;
    }

    @Override
    public Devis update(Devis devis) {
        if (devis.getId() == null) {
            throw new IllegalArgumentException("Cannot update a Devis without an ID");
        }

        StringBuilder sql = new StringBuilder("UPDATE Devis SET ");
        List<Object> params = new ArrayList<>();
        boolean needComma = false;

        if (devis.getEstimatedPrice() != null) {
            sql.append("estimatedPrice = ?");
            params.add(devis.getEstimatedPrice());
            needComma = true;
        }
        if (devis.getIssueDate() != null) {
            if (needComma) sql.append(", ");
            sql.append("issueDate = ?");
            params.add(Date.valueOf(devis.getIssueDate()));
            needComma = true;
        }
        if (devis.getValidityDate() != null) {
            if (needComma) sql.append(", ");
            sql.append("validityDate = ?");
            params.add(Date.valueOf(devis.getValidityDate()));
            needComma = true;
        }
        if (devis.getAccepted() != null) {
            if (needComma) sql.append(", ");
            sql.append("accepted = ?");
            params.add(devis.getAccepted());
            needComma = true;
        }
        if (devis.getProjet() != null && devis.getProjet().getId() != null) {
            if (needComma) sql.append(", ");
            sql.append("project_id = ?");
            params.add(devis.getProjet().getId());
        }

        sql.append(" WHERE id = ?");
        params.add(devis.getId());

        // Add this line to make the UPDATE statement return the updated row
        sql.append(" RETURNING *");

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {
                    for (int i = 0; i < params.size(); i++) {
                        stmt.setObject(i + 1, params.get(i));
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Updating devis failed, no rows affected.");
                    }

                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            devis = Mappers.mapResultSetToDevis(rs);
                        } else {
                            throw new SQLException("Updating devis failed, no rows returned.");
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

        return devis;
    }



}