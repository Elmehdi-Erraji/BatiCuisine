package Services;

import Entities.Composants;
import Entities.MainDoeuvre;
import Entities.Materiaux;
import Entities.Projet;
import repositories.Projet.ProjetRepository;

import java.util.List;
import java.util.Optional;

public class ProjetService {
    private final ProjetRepository projetRepository;

    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    public Projet createProjet(Projet projet) {
        return projetRepository.save(projet);
    }

    public Projet createProjetWithComponents(Projet projet) {
        Projet savedProjet = projetRepository.save(projet);

        MateriauxService materiauxService =  new MateriauxService();
        MainDœuvreService mainDœuvreService =  new MainDœuvreService();

        List<Composants> composants =  projet.getComposants();

        composants.forEach(composant -> {
            if (composant instanceof Materiaux materiaux) {

                composant.setProjet(savedProjet);
                materiauxService.createMateriaux(materiaux);

            } else if (composant instanceof MainDoeuvre mainDoeuvre) {

                composant.setProjet(savedProjet);
                mainDœuvreService.createMainDœuvre(mainDoeuvre);

            }
        });

        return savedProjet;
    }


    public Optional<Projet> getProjetById(Integer id) {
        return projetRepository.findById(id);
    }

    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    public Projet updateProjet(Projet projet) {
        return projetRepository.save(projet);
    }

    public void deleteProjet(Integer id) {
        projetRepository.deleteById(id);
    }
}
