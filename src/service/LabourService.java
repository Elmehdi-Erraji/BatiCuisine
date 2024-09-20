package service;


import domain.entities.Labour;
import repository.Interfaces.LabourRepository;
import repository.implimentation.LabourRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class LabourService {
    private final LabourRepository labourRepository;

    public LabourService() {
        this.labourRepository = new LabourRepositoryImpl();
    }

    public Labour createLabour(Labour labour) {
        return labourRepository.save(labour);
    }

    public Optional<Labour> getLabourById(Integer id) {
        return labourRepository.findById(id);
    }

    public List<Labour> getAllLabours() {
        return labourRepository.findAll();
    }

    public Labour updateLabour(Labour labour) {
        return labourRepository.save(labour);
    }

    public void deleteLabour(Integer id) {
        labourRepository.deleteById(id);
    }
}
