package repository.Interfaces;

import domain.entities.Labour;

import java.util.List;
import java.util.Optional;

public interface LaborRepository {
    Labour save(Labour labor);
    Optional<Labour> findById(Integer id);
    List<Labour> findAll();
    void deleteById(Integer id);
}