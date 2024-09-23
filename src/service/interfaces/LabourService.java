package service.interfaces;

import domain.entities.Labour;

import java.util.List;
import java.util.Optional;

public interface LabourService {
    Labour createLabour(Labour labour);

    Optional<Labour> getLabourById(Integer id);

    List<Labour> getAllLabours();

    Labour updateLabour(Labour labour);

    void deleteLabour(Integer id);
}
