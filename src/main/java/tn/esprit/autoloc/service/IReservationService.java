package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;

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
}
