package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;

/**
 * DTO de requête pour le service creerReservationEtContratAssocie.
 * Regroupe les informations nécessaires à la création simultanée
 * d'une réservation et de son contrat associé.
 */
@Data
public class ReservationWithContratRequestDTO {

    // ── Réservation ──────────────────────────────────────────────────────────
    @NotNull(message = "La date de début de la réservation est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin de la réservation est obligatoire")
    private LocalDate dateFin;

    private StatutReservation statutReservation;

    @NotNull(message = "L'identifiant du client est obligatoire")
    private Long clientId;

    @NotNull(message = "L'identifiant du véhicule est obligatoire")
    private Long vehiculeId;

    // ── Contrat associé ──────────────────────────────────────────────────────
    private LocalDate dateSignature;   // si null → LocalDate.now()

    // Le montant est calculé automatiquement : tarifJournalier × nbJours
    // mais peut être surchargé ici s'il est renseigné
    private java.math.BigDecimal montantTotal;
}

