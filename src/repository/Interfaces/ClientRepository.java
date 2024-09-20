package repository.Interfaces;

import domain.entities.Client ;

import java.util.List;

public interface ClientRepository {
    void create(Client  user);
    Client  findById(int id);
    List<Client > findAll();
    void update(Client  user);
    void delete(int id);
}

