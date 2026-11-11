package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de lecture d'un contrat.
 */
@Data
@Builder
public class ContratResponse {

    private Long idContrat;
    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private boolean valide;

    // Référence à la réservation
    private Long reservationId;

    // Liste résumée des paiements associés
    private List<PaiementResponse> paiements;
}

