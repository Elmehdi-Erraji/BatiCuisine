package service;

import domain.entities.Material;
import repository.implimentation.MaterialsRepositoryImpl;
import java.util.List;
import java.util.Optional;

public class MaterialsService {
    private final MaterialsRepositoryImpl materialsRepository;

    public MaterialsService(MaterialsRepositoryImpl materialsRepository) {
        this.materialsRepository = materialsRepository;
    }

    public Material createMaterials(Material materials) {
        return materialsRepository.save(materials);
    }

    public Optional<Material> getMaterialsById(Integer id) {
        return materialsRepository.findById(id);
    }

    public List<Material> getAllMaterials() {
        return materialsRepository.findAll();
    }

    public Material updateMaterials(Material materials) {
        return materialsRepository.save(materials);
    }

    public void deleteMaterials(Integer id) {
        materialsRepository.deleteById(id);
    }
}
