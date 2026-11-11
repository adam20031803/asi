package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ClientRequest;
import tn.esprit.autoloc.web.dto.ClientResponse;

import java.time.LocalDate;

/**
 * Mapper manuel entre l'entité {@link Client} et ses DTO.
 */
@Component
public class ClientMapper {

    public Client toEntity(ClientRequest dto) {
        Client client = new Client();
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setEmail(dto.getEmail());
        client.setTelephone(dto.getTelephone());
        client.setNumPermis(dto.getNumPermis());
        client.setDateInscription(LocalDate.now());
        return client;
    }

    public ClientResponse toResponse(Client client) {
        return ClientResponse.builder()
                .idClient(client.getIdClient())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .telephone(client.getTelephone())
                .numPermis(client.getNumPermis())
                .dateInscription(client.getDateInscription())
                .build();
    }
}

