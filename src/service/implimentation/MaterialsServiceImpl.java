package service.implimentation;

import domain.entities.Material;
import repository.Interfaces.MaterialsRepository;
import repository.implimentation.MaterialsRepositoryImpl;
import service.interfaces.MaterialsService;

import java.util.List;
import java.util.Optional;

public class MaterialsServiceImpl implements MaterialsService {
    private final MaterialsRepository materialsRepository;

    public MaterialsServiceImpl() {
        this.materialsRepository = new MaterialsRepositoryImpl();
    }

    @Override
    public Material createMaterial(Material material) {
        return materialsRepository.save(material); // Call on the instance
    }

    @Override
    public Optional<Material> getMaterialById(Integer id) {
        return materialsRepository.findById(id); // Call on the instance
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialsRepository.findAll(); // Call on the instance
    }

    @Override
    public Material updateMaterial(Material material) {
        return materialsRepository.save(material); // Call on the instance
    }

    @Override
    public void deleteMaterial(Integer id) {
        materialsRepository.deleteById(id); // Call on the instance
    }
}
