package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.autoloc.domain.RoleEmploye;

/**
 * DTO de lecture d'un employé.
 */
@Data
@Builder
public class EmployeResponse {

    private Long idEmploye;
    private String nom;
    private String prenom;
    private RoleEmploye role;

    // Informations résumées sur l'agence
    private Long agenceId;
    private String agenceNom;
}

