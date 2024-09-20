package Utils;

import Entities.*;
import Enums.EtatProject;
import Enums.TypeComposant;

import java.sql.ResultSet;
import java.sql.SQLException;

public class mappers {

    static public Projet mapResultSetToProjet(ResultSet rs) throws SQLException {
        return new Projet(
                rs.getInt("id"),
                rs.getString("projectName"),
                rs.getDouble("profit"),
                rs.getDouble("totalCost"),
                rs.getDouble("discount"),
                EtatProject.valueOf(rs.getString("status"))
        );
    }

    static public Devis mapResultSetToDevis(ResultSet rs) throws SQLException {
        Projet projet = new Projet();
        return new Devis(
                rs.getInt("id"),
                rs.getDouble("estimatedPrice"),
                rs.getDate("issueDate").toLocalDate(),
                rs.getDate("validityDate").toLocalDate(),
                rs.getBoolean("accepted"),
                projet);
    }

    static public Devis mapResultSetToDevisAndPorject(ResultSet rs) throws SQLException {
        Projet projet =  Mappers.mapResultSetToProjet(rs);

        return new Devis(
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

    static public MainDoeuvre mapResultSetToMainDœuvre(ResultSet rs) throws SQLException {
        return new MainDoeuvre(
                rs.getString("name"),
                rs.getDouble("taxRate"),
                TypeComposant.MAINDOUVRE,
                rs.getInt("id"),
                rs.getDouble("hourlyRate"),
                rs.getDouble("workHoursCount"),
                rs.getDouble("productivityRate")
        );
    }

    static public Materiaux mapResultSetToMateriaux(ResultSet rs) throws SQLException {
        return new Materiaux(
                rs.getString("name"),
                rs.getDouble("taxRate"),
                TypeComposant.MATERIEL,
                rs.getInt("id"),
                rs.getDouble("unitCost"),
                rs.getDouble("quantity"),
                rs.getDouble("transportCost"),
                rs.getDouble("qualityCoefficient")
        );
    }

}
