package service;

import domain.entities.Material;
import repository.Interfaces.MaterialsRepository;
import repository.implimentation.MaterialsRepositoryImpl;
import config.dbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MaterialsService {
    private final MaterialsRepository materialsRepository;

    public MaterialsService() throws SQLException {
        Connection connection = dbConnection.getInstance().getConnection();
        this.materialsRepository = new MaterialsRepositoryImpl(connection);
    }

    public MaterialsService(MaterialsRepository materialsRepository) {
        this.materialsRepository = materialsRepository;
    }

    public Material createMaterial(Material material) {
        return materialsRepository.save(material);
    }

    public Optional<Material> getMaterialById(Integer id) {
        return materialsRepository.findById(id);
    }

    public List<Material> getAllMaterials() {
        return materialsRepository.findAll();
    }

    public Material updateMaterial(Material material) {
        return materialsRepository.save(material);
    }

    public void deleteMaterial(Integer id) {
        materialsRepository.deleteById(id);
    }
}
