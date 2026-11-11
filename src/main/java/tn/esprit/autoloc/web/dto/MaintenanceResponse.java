package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de lecture d'une maintenance.
 */
@Data
@Builder
public class MaintenanceResponse {

    private Long idMaintenance;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;

    // Informations résumées sur le véhicule
    private Long vehiculeId;
    private String vehiculeImmatriculation;
    private String vehiculeMarque;
    private String vehiculeModele;
}

