package repository.Interfaces;

import domain.entities.Client ;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Integer id);
    List<Client> findAll();
    void deleteById(Integer id);
    List<Client> findByProfessional(Boolean isProfessional);
}

