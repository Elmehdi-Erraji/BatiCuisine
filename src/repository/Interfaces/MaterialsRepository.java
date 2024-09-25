package repository.Interfaces;

import domain.entities.Material;

import java.util.List;
import java.util.Optional;

public interface MaterialsRepository {
    Material save(Material material);
    Optional<Material> findById(Integer id);
    List<Material> findAll();
    void delete(Material material);
}

