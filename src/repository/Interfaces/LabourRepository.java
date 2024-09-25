package repository.Interfaces;

import domain.entities.Labour;

import java.util.List;
import java.util.Optional;

public interface LabourRepository {
    Labour save(Labour labour);
    Optional<Labour> findById(Integer id);
    List<Labour> findAll();
    void deleteById(Labour labour);
}