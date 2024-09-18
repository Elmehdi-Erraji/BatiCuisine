package service;

import domain.entities.Labour;
import repository.implimentation.LabourRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class LabourService {
    private final LabourRepositoryImpl LabourRepository;

    public LabourService(LabourRepositoryImpl LabourRepository) {
        this.LabourRepository = LabourRepository;
    }

    public Labour createLabour(Labour Labour) {
        return LabourRepository.save(Labour);
    }

    public Optional<Labour> getLabourById(Integer id) {
        return LabourRepository.findById(id);
    }

    public List<Labour> getAllLabour() {
        return LabourRepository.findAll();
    }

    public Labour updateLabour(Labour Labour) {
        return LabourRepository.save(Labour);
    }

    public void deleteLabour(Integer id) {
        LabourRepository.deleteById(id);
    }
}
