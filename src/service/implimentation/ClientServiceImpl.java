package service;


import domain.entities.Client;
import repository.Interfaces.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientServiceInterface {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> getClientById(Integer id) {
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }

    @Override
    public List<Client> getProfessionalClients() {
        return clientRepository.findByProfessional(true);
    }

    @Override
    public List<Client> getNonProfessionalClients() {
        return clientRepository.findByProfessional(false);
    }
}
