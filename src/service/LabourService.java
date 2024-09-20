package service;

import config.dbConnection;
import domain.entities.Labour;
import repository.Interfaces.LaborRepository;
import repository.implimentation.LabourRepositoryImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LabourService {
    private final LaborRepository labourRepository;

    public LabourService() throws SQLException {
        Connection connection = config.dbConnection.getInstance().getConnection();
        this.labourRepository = new LabourRepositoryImpl(connection);
    }

    public Labour createLabour(Labour labour) {
        return labourRepository.save(labour);
    }

    public Optional<Labour> getLabourById(Integer id) {
        return labourRepository.findById(id);
    }

    public List<Labour> getAllLabour() {
        return labourRepository.findAll();
    }

    public Labour updateLabour(Labour labour) {
        return labourRepository.save(labour);
    }

    public void deleteLabour(Integer id) {
        labourRepository.deleteById(id);
    }
}
