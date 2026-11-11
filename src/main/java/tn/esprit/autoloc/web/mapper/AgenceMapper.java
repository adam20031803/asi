package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceRequest;
import tn.esprit.autoloc.web.dto.AgenceResponse;

/**
 * Mapper manuel entre l'entité {@link Agence} et ses DTO.
 */
@Component
public class AgenceMapper {

    public Agence toEntity(AgenceRequest dto) {
        Agence agence = new Agence();
        agence.setNom(dto.getNom());
        agence.setVille(dto.getVille());
        agence.setAdresse(dto.getAdresse());
        agence.setTelephone(dto.getTelephone());
        return agence;
    }

    public AgenceResponse toResponse(Agence agence) {
        return AgenceResponse.builder()
                .idAgence(agence.getIdAgence())
                .nom(agence.getNom())
                .ville(agence.getVille())
                .adresse(agence.getAdresse())
                .telephone(agence.getTelephone())
                .build();
    }
}

