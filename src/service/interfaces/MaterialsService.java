package service.interfaces;

import domain.entities.Material;

import java.util.List;
import java.util.Optional;

public interface MaterialsService {
    Material createMaterial(Material material);

    Optional<Material> getMaterialById(Integer id);

    List<Material> getAllMaterials();

    Material updateMaterial(Material material);

    void delete(Material material);
}
