package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de lecture d'une agence (exposé par l'API, sans les collections enfants).
 */
@Data
@Builder
public class AgenceResponse {

    private Long idAgence;
    private String nom;
    private String ville;
    private String adresse;
    private String telephone;
}

