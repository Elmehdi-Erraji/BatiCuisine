package service.interfaces;

import domain.entities.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client createClient(Client client);

    Optional<Client> getClientById(Integer id);

    List<Client> getAllClients();

    Client updateClient(Client client);

    void deleteClient(Integer id);

    List<Client> getProfessionalClients();

    List<Client> getNonProfessionalClients();
}
