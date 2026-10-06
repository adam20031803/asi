package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Client;

import java.util.List;

public interface IClientService {

    Client createClient(Client client);

    Client updateClient(Long id, Client client);

    Client getClientById(Long id);

    List<Client> getAllClients();

    void deleteClient(Long id);

    Client getClientByEmail(String email);

    Client getClientByNumPermis(String numPermis);
}
