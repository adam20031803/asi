package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de création / mise à jour d'un contrat.
 */
@Data
public class ContratRequest {

    private LocalDate dateSignature;

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.00", inclusive = true, message = "Le montant total ne peut pas être négatif")
    private BigDecimal montantTotal;

    private boolean valide;

    @NotNull(message = "L'identifiant de la réservation est obligatoire")
    private Long reservationId;
}

