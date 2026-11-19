package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de réponse pour les services d'affectation de contrat.
 */
@Data
@Builder
public class ContratDTO {

    private Long idContrat;
    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private boolean valide;

    // Réservation associée
    private Long reservationId;
    private LocalDate reservationDateDebut;
    private LocalDate reservationDateFin;

    // Client de la réservation
    private Long clientId;
    private String clientNomComplet;

    // Véhicule de la réservation
    private Long vehiculeId;
    private String vehiculeImmatriculation;

    // Paiements liés
    private List<PaiementResponse> paiements;
}

