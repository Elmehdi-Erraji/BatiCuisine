package service;

import domain.entities.Client;
import repository.Interfaces.CrudRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private final CrudRepository<Client, Integer> clientRepository;

    public ClientService(CrudRepository<Client, Integer> clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<Client> addClient(Client client) {
        return clientRepository.create(client);
    }

    public Optional<Client> getClientById(int id) {
        return clientRepository.findById(id);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> updateClient(Client client) {
        return clientRepository.update(client);
    }

    public void deleteClient(int id) {
        clientRepository.delete(id);
    }
}
