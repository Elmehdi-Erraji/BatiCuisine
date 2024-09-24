package service.implimentation;


import domain.entities.Labour;
import repository.Interfaces.LabourRepository;
import repository.implimentation.LabourRepositoryImpl;
import service.interfaces.LabourService;

import java.util.List;
import java.util.Optional;

public class LabourServiceImpl implements LabourService {
    private final LabourRepository labourRepository;

    public LabourServiceImpl() {
        this.labourRepository = new LabourRepositoryImpl();
    }

    @Override
    public Labour createLabour(Labour labour) {
        return labourRepository.save(labour);
    }

    @Override
    public Optional<Labour> getLabourById(Integer id) {
        return labourRepository.findById(id);
    }

    @Override
    public List<Labour> getAllLabours() {
        return labourRepository.findAll();
    }

    @Override
    public Labour updateLabour(Labour labour) {
        return labourRepository.save(labour);
    }

    @Override
    public void deleteLabour(Integer id) {
        labourRepository.deleteById(id);
    }
}
