package repository.implimentation;


import domain.entities.Quote;
import repository.Interfaces.QuoteRepository;
import service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteRepositoryImpl implements QuoteRepository {
    private final Connection connection;
    private final UserService userService;

    public QuoteRepositoryImpl(Connection connection, UserService userService) {
        this.connection = connection;
        this.userService = userService;
    }

    @Override
    public Quote save(Quote Quote) {
        String sql = Quote.getId() == null ? "INSERT INTO Quotes (estimatedPrice, issueDate, validityDate, accepted, project_id) VALUES (?, ?, ?, ?, ?)" : "UPDATE Quote SET estimatedPrice = ?, issueDate = ?, validityDate = ?, accepted = ?, project_id = ? WHERE id = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setDouble(1, Quote.getEstimatedPrice());
                    stmt.setDate(2, Date.valueOf(Quote.getIssueDate()));
                    stmt.setDate(3, Date.valueOf(Quote.getValidityDate()));
                    stmt.setBoolean(4, Quote.getAccepted());
                    stmt.setInt(5, Quote.getProjectId());

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

        return Quote;
    }


    @Override
    public Optional<Quote> findById(Integer id) {
        String sql = "SELECT * FROM Quotes WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return Optional.of(mapResultSetToQuote(rs));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        return Optional.empty();
    }

    @Override
    public List<Quote> findAll() {
        List<Quote> QuoteList = new ArrayList<>();
        String sql = "SELECT * FROM Quotes";
                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        QuoteList.add(mapResultSetToQuote(rs));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        return QuoteList;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM Quotes WHERE id = ?";

                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }




    @Override
    public List<Quote> findByProjectId(Integer projectId) {
        List<Quote> QuoteList = new ArrayList<>();
        String sql = "SELECT * FROM Quotes WHERE project_id = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, projectId);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        QuoteList.add(mapResultSetToQuote(rs));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        return QuoteList;
    }

    private Quote mapResultSetToQuote(ResultSet rs) throws SQLException {
        return new Quote(rs.getInt("id"), rs.getDouble("estimatedPrice"), rs.getDate("issueDate").toLocalDate(), rs.getDate("validityDate").toLocalDate(), rs.getBoolean("accepted"), rs.getInt("project_id"));
    }
}