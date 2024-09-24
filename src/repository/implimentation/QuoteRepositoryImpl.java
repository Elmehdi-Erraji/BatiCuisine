package repository.implimentation;

import config.DBConnection;
import domain.entities.Client;
import domain.entities.Quote;
import repository.Interfaces.QuoteRepository;
import utils.Mappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteRepositoryImpl implements QuoteRepository {
    private DBConnection dbConnection;
    private Connection connection = null;

    @Override
    public Quote save(Quote Quote) {
        String sql = Quote.getId() == null ? "INSERT INTO quotes (estimatedprice, issuedate, validitydate, accepted, project_id) VALUES (?, ?, ?, ?, ?)" : "UPDATE Quote SET estimatedPrice = ?, issueDate = ?, validityDate = ?, accepted = ?, project_id = ? WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setDouble(1, Quote.getEstimatedPrice());
                    stmt.setDate(2, Date.valueOf(Quote.getIssueDate()));
                    stmt.setDate(3, Date.valueOf(Quote.getValidityDate()));
                    stmt.setBoolean(4, Quote.getAccepted());
                    stmt.setInt(5, Quote.getProject().getId());

                    if (Quote.getId() != null) {
                        stmt.setInt(6, Quote.getId());
                    }

                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating Quote failed, no rows affected.");
                    }

                    if (Quote.getId() == null) {
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                Quote.setId(generatedKeys.getInt(1));
                            } else {
                                throw new SQLException("Creating Quote failed, no ID obtained.");
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


        return Quote;
    }

    @Override
    public Optional<Quote> findById(Integer id) {
        String sql = "SELECT * FROM quotes WHERE id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(Mappers.mapResultSetToQuote(rs));
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
    public List<Quote> findAll() {
        List<Quote> QuoteList = new ArrayList<>();
        String sql = "SELECT * FROM quotes";
        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        QuoteList.add(Mappers.mapResultSetToQuote(rs));
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


        return QuoteList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM quotes WHERE id = ?";
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
    public List<Quote> findQuoteWithProjectById(Client client) {
        List<Quote> devisList = new ArrayList<>();
        String sql = "Select * FROM quotes d JOIN projects p ON d.project_id = p.id WHERE p.client_id = ?";

        try {
            dbConnection = DBConnection.getInstance();
            if (dbConnection != null) {
                connection = dbConnection.getConnection();

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, client.getId());
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        devisList.add(Mappers.mapResultSetToQuotesAndPorject(rs));
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
    public Quote update(Quote Quote) {
        if (Quote.getId() == null) {
            throw new IllegalArgumentException("Cannot update a Quote without an ID");
        }

        StringBuilder sql = new StringBuilder("UPDATE quotes SET ");
        List<Object> params = new ArrayList<>();
        boolean needComma = false;

        if (Quote.getEstimatedPrice() != null) {
            sql.append("estimatedprice = ?");
            params.add(Quote.getEstimatedPrice());
            needComma = true;
        }
        if (Quote.getIssueDate() != null) {
            if (needComma) sql.append(", ");
            sql.append("issuedate = ?");
            params.add(Date.valueOf(Quote.getIssueDate()));
            needComma = true;
        }
        if (Quote.getValidityDate() != null) {
            if (needComma) sql.append(", ");
            sql.append("validitydate = ?");
            params.add(Date.valueOf(Quote.getValidityDate()));
            needComma = true;
        }
        if (Quote.getAccepted() != null) {
            if (needComma) sql.append(", ");
            sql.append("accepted = ?");
            params.add(Quote.getAccepted());
            needComma = true;
        }
        if (Quote.getProject() != null && Quote.getProject().getId() != null) {
            if (needComma) sql.append(", ");
            sql.append("project_id = ?");
            params.add(Quote.getProject().getId());
        }

        sql.append(" WHERE id = ?");
        params.add(Quote.getId());

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
                        throw new SQLException("Updating Quote failed, no rows affected.");
                    }

                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            Quote = Mappers.mapResultSetToQuote(rs);
                        } else {
                            throw new SQLException("Updating Quote failed, no rows returned.");
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

        return Quote;
    }



}