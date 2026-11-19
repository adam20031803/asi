package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;

/**
 * DTO de réponse enrichi pour les services d'affectation de réservation.
 * Contient l'identifiant du contrat associé si présent.
 */
@Data
@Builder
public class ReservationResponseDTO {

    private Long idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutReservation statut;

    // Client affecté
    private Long clientId;
    private String clientNom;
    private String clientPrenom;
    private String clientNomComplet;

    // Véhicule affecté
    private Long vehiculeId;
    private String vehiculeImmatriculation;
    private String vehiculeMarque;
    private String vehiculeModele;

    // Contrat associé (si existant)
    private Long contratId;
}

