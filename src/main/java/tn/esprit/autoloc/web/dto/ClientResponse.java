package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de lecture d'un client.
 */
@Data
@Builder
public class ClientResponse {

    private Long idClient;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String numPermis;
    private LocalDate dateInscription;
}

