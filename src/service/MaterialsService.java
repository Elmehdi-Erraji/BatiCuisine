package service;

import domain.entities.Material;
import repository.Interfaces.MaterialsRepository;
import repository.implimentation.MaterialsRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class MaterialsService {
    private final MaterialsRepository materialsRepository;

    public MaterialsService() {
        this.materialsRepository = new MaterialsRepositoryImpl();
    }

    public Material createMaterial(Material material) {
        return materialsRepository.save(material); // Call on the instance
    }

    public Optional<Material> getMaterialById(Integer id) {
        return materialsRepository.findById(id); // Call on the instance
    }

    public List<Material> getAllMaterials() {
        return materialsRepository.findAll(); // Call on the instance
    }

    public Material updateMaterial(Material material) {
        return materialsRepository.save(material); // Call on the instance
    }

    public void deleteMaterial(Integer id) {
        materialsRepository.deleteById(id); // Call on the instance
    }
}
