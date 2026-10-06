package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IClientService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client createClient(Client client) {
        if (client == null) {
            throw new BusinessException("Les informations du client ne peuvent pas être nulles");
        }
        if (client.getEmail() != null && clientRepository.existsByEmail(client.getEmail())) {
            throw new BusinessException("Un client avec l'email '" + client.getEmail() + "' existe déjà");
        }
        if (client.getNumPermis() != null && clientRepository.existsByNumPermis(client.getNumPermis())) {
            throw new BusinessException("Un client avec le numéro de permis '" + client.getNumPermis() + "' existe déjà");
        }
        if (client.getDateInscription() == null) {
            client.setDateInscription(LocalDate.now());
        }
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long id, Client updatedClient) {
        if (updatedClient == null) {
            throw new BusinessException("Les données de mise à jour du client ne peuvent pas être nulles");
        }
        Client existingClient = getClientById(id);

        if (updatedClient.getEmail() != null && !updatedClient.getEmail().equalsIgnoreCase(existingClient.getEmail())) {
            if (clientRepository.existsByEmail(updatedClient.getEmail())) {
                throw new BusinessException("L'email '" + updatedClient.getEmail() + "' est déjà utilisé par un autre client");
            }
            existingClient.setEmail(updatedClient.getEmail());
        }

        if (updatedClient.getNumPermis() != null && !updatedClient.getNumPermis().equalsIgnoreCase(existingClient.getNumPermis())) {
            if (clientRepository.existsByNumPermis(updatedClient.getNumPermis())) {
                throw new BusinessException("Le numéro de permis '" + updatedClient.getNumPermis() + "' est déjà utilisé par un autre client");
            }
            existingClient.setNumPermis(updatedClient.getNumPermis());
        }

        if (updatedClient.getNom() != null) {
            existingClient.setNom(updatedClient.getNom());
        }
        if (updatedClient.getPrenom() != null) {
            existingClient.setPrenom(updatedClient.getPrenom());
        }
        if (updatedClient.getTelephone() != null) {
            existingClient.setTelephone(updatedClient.getTelephone());
        }

        return clientRepository.save(existingClient);
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucun client avec l'identifiant : " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun client trouvé avec l'email : " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientByNumPermis(String numPermis) {
        return clientRepository.findByNumPermis(numPermis)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun client trouvé avec le permis : " + numPermis));
    }
}
