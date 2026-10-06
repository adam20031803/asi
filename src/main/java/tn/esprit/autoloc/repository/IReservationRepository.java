package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;

import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByClient_IdClient(Long idClient);

    List<Reservation> findByVehicule_IdVehicule(Long idVehicule);

    List<Reservation> findByStatut(StatutReservation statut);
}
