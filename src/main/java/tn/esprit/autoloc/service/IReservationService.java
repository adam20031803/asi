package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;

import java.util.List;

public interface IReservationService {

    Reservation createReservation(Reservation reservation);

    Reservation updateReservation(Long id, Reservation reservation);

    Reservation getReservationById(Long id);

    List<Reservation> getAllReservations();

    void deleteReservation(Long id);

    List<Reservation> getReservationsByClient(Long idClient);

    List<Reservation> getReservationsByVehicule(Long idVehicule);

    List<Reservation> getReservationsByStatut(StatutReservation statut);

    Reservation annulerReservation(Long idReservation);

    Reservation confirmerReservation(Long idReservation);

    // ── Services d'affectation ───────────────────────────────────────────────

    /**
     * Affecte une réservation existante à un client identifié par son nom et prénom.
     * Cherche le premier client correspondant ; lève ResourceNotFoundException si absent.
     */
    ReservationResponseDTO affecterReservationAClient(Long idReservation,
                                                      String nom,
                                                      String prenom);

    /**
     * Crée simultanément une réservation et le contrat associé à partir d'un DTO composite.
     * Le montant du contrat est calculé automatiquement (tarifJournalier × nbJours)
     * si montantTotal n'est pas fourni.
     */
    ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO);
}
