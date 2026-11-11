package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.autoloc.domain.ModePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de lecture d'un paiement.
 */
@Data
@Builder
public class PaiementResponse {

    private Long idPaiement;
    private BigDecimal montant;
    private LocalDate datePaiement;
    private ModePaiement modePaiement;

    // Référence au contrat parent
    private Long contratId;
}

