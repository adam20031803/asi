package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;

/**
 * DTO de lecture d'une réservation.
 */
@Data
@Builder
public class ReservationResponse {

    private Long idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutReservation statut;

    // Informations résumées sur le client
    private Long clientId;
    private String clientNomComplet;

    // Informations résumées sur le véhicule
    private Long vehiculeId;
    private String vehiculeImmatriculation;
    private String vehiculeMarque;
    private String vehiculeModele;
}

