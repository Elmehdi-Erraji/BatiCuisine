package utils;


import domain.entities.*;
import domain.enums.ComponentType;
import domain.enums.ProjectStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mappers {

    static public Project mapResultSetToProjet(ResultSet rs) throws SQLException {
        return new Project(
                rs.getInt("id"),
                rs.getString("projectName"),
                rs.getDouble("profit"),
                rs.getDouble("totalCost"),
                rs.getDouble("discount"),
                ProjectStatus.valueOf(rs.getString("status"))
        );
    }

    static public Quote mapResultSetToQuote(ResultSet rs) throws SQLException {
        Project projet = new Project();
        return new Quote(
                rs.getInt("id"),
                rs.getDouble("estimatedPrice"),
                rs.getDate("issueDate").toLocalDate(),
                rs.getDate("validityDate").toLocalDate(),
                rs.getBoolean("accepted"),
                projet);
    }

    static public Quote mapResultSetToDevisAndPorject(ResultSet rs) throws SQLException {
        Project projet =  Mappers.mapResultSetToProjet(rs);

        return new Quote(
                rs.getInt("id"),
                rs.getDouble("estimatedPrice"),
                rs.getDate("issueDate").toLocalDate(),
                rs.getDate("validityDate").toLocalDate(),
                rs.getBoolean("accepted"),
                projet);
    }

    static public Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("phoneNumber"),
                rs.getBoolean("isProfessional")
        );
    }

    static public Labour mapResultSetToMainDœuvre(ResultSet rs) throws SQLException {
        return new Labour(
                rs.getString("name"),
                rs.getDouble("taxRate"),
                ComponentType.LABOR,
                rs.getInt("id"),
                rs.getDouble("hourlyRate"),
                rs.getDouble("workHoursCount"),
                rs.getDouble("productivityRate")
        );
    }

    static public Material mapResultSetToMateriaux(ResultSet rs) throws SQLException {
        return new Material(
                rs.getString("name"),
                rs.getDouble("taxRate"),
                ComponentType.MATERIEL,
                rs.getInt("id"),
                rs.getDouble("unitCost"),
                rs.getDouble("quantity"),
                rs.getDouble("transportCost"),
                rs.getDouble("qualityCoefficient")
        );
    }

}
