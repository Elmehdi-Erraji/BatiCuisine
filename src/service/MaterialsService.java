package Services;

import Entities.Materiaux;
import repositories.Materiaux.MateriauxRepository;
import repositories.Materiaux.MateriauxRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class MateriauxService {
    private final MateriauxRepository materiauxRepository;

    public MateriauxService() {
        this.materiauxRepository = new MateriauxRepositoryImpl();
    }

    public Materiaux createMateriaux(Materiaux materiaux) {
        return materiauxRepository.save(materiaux);
    }

    public Optional<Materiaux> getMateriauxById(Integer id) {
        return materiauxRepository.findById(id);
    }

    public List<Materiaux> getAllMateriaux() {
        return materiauxRepository.findAll();
    }

    public Materiaux updateMateriaux(Materiaux materiaux) {
        return materiauxRepository.save(materiaux);
    }

    public void deleteMateriaux(Integer id) {
        materiauxRepository.deleteById(id);
    }
}
