package service.implimentation;


import domain.entities.Client;
import repository.Interfaces.ClientRepository;
import service.interfaces.ClientService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientServiceImpl implements ClientService {
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
    public Optional<Client> getClientByName(String name) {
        return clientRepository.findByName(name);
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
    public void deleteClient(Client client) {
        clientRepository.deleteById(client);
    }

    public void checkAndUpdateClientStatus(Integer clientId) {
        int rejectedCount = clientRepository.getRejectedQuotesCount(clientId);

        if (rejectedCount >= 2) {
            clientRepository.updateClientStatus(clientId, true);

            throw new IllegalStateException("Client has more than 5 rejected quotes and is blocked.");
        }
        System.out.println("Client is allowed to create new projects.");
    }

}
