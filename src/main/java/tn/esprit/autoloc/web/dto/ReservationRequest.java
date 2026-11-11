package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;

/**
 * DTO de création / mise à jour d'une réservation.
 */
@Data
public class ReservationRequest {

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Future(message = "La date de fin doit être dans le futur")
    private LocalDate dateFin;

    private StatutReservation statut;

    @NotNull(message = "L'identifiant du client est obligatoire")
    private Long clientId;

    @NotNull(message = "L'identifiant du véhicule est obligatoire")
    private Long vehiculeId;
}

