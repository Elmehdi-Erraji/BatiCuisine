package repository.Interfaces;

import domain.entities.Client ;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Integer id);
    Optional<Client> findByName(String name);
    List<Client> findAll();
    void deleteById(Client client);
    List<Client> findByProfessional(Boolean isProfessional);
}

