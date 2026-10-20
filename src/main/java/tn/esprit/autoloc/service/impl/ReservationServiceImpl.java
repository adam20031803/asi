package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {
        if (reservation == null) {
            throw new BusinessException("Les données de la réservation ne peuvent pas être nulles");
        }
        if (reservation.getIdReservation() != null) {
            throw new BusinessException("L'identifiant de la réservation est généré automatiquement : il ne doit pas être fourni à la création");
        }
        if (reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            throw new BusinessException("Les dates de début et de fin sont obligatoires");
        }
        if (reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new BusinessException("La date de fin ne peut pas être antérieure à la date de début");
        }
        if (reservation.getStatut() == null) {
            reservation.setStatut(StatutReservation.EN_ATTENTE);
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        if (updatedReservation == null) {
            throw new BusinessException("Les données de mise à jour ne peuvent pas être nulles");
        }
        Reservation existingReservation = getReservationById(id);

        if (updatedReservation.getDateDebut() != null && updatedReservation.getDateFin() != null) {
            if (updatedReservation.getDateFin().isBefore(updatedReservation.getDateDebut())) {
                throw new BusinessException("La date de fin ne peut pas être antérieure à la date de début");
            }
            existingReservation.setDateDebut(updatedReservation.getDateDebut());
            existingReservation.setDateFin(updatedReservation.getDateFin());
        }

        if (updatedReservation.getStatut() != null) {
            existingReservation.setStatut(updatedReservation.getStatut());
        }
        if (updatedReservation.getClient() != null) {
            existingReservation.setClient(updatedReservation.getClient());
        }
        if (updatedReservation.getVehicule() != null) {
            existingReservation.setVehicule(updatedReservation.getVehicule());
        }

        return reservationRepository.save(existingReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucune réservation avec l'identifiant : " + id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByClient(Long idClient) {
        return reservationRepository.findByClient_IdClient(idClient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByVehicule(Long idVehicule) {
        return reservationRepository.findByVehicule_IdVehicule(idVehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    @Override
    public Reservation annulerReservation(Long idReservation) {
        Reservation reservation = getReservationById(idReservation);
        reservation.setStatut(StatutReservation.ANNULEE);
        if (reservation.getVehicule() != null) {
            reservation.getVehicule().setStatut(StatutVehicule.DISPONIBLE);
            vehiculeRepository.save(reservation.getVehicule());
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation confirmerReservation(Long idReservation) {
        Reservation reservation = getReservationById(idReservation);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        if (reservation.getVehicule() != null) {
            reservation.getVehicule().setStatut(StatutVehicule.LOUE);
            vehiculeRepository.save(reservation.getVehicule());
        }
        return reservationRepository.save(reservation);
    }
}
