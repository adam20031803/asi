package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ReservationRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;

/**
 * Mapper manuel entre l'entité {@link Reservation} et ses DTO.
 */
@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;

    public Reservation toEntity(ReservationRequest dto) {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutReservation.EN_ATTENTE);

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client introuvable avec l'identifiant : " + dto.getClientId()));
        reservation.setClient(client);

        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'identifiant : " + dto.getVehiculeId()));
        reservation.setVehicule(vehicule);

        return reservation;
    }

    public ReservationResponse toResponse(Reservation reservation) {
        Client client = reservation.getClient();
        Vehicule vehicule = reservation.getVehicule();

        return ReservationResponse.builder()
                .idReservation(reservation.getIdReservation())
                .dateDebut(reservation.getDateDebut())
                .dateFin(reservation.getDateFin())
                .statut(reservation.getStatut())
                .clientId(client != null ? client.getIdClient() : null)
                .clientNomComplet(client != null ? client.getPrenom() + " " + client.getNom() : null)
                .vehiculeId(vehicule != null ? vehicule.getIdVehicule() : null)
                .vehiculeImmatriculation(vehicule != null ? vehicule.getImmatriculation() : null)
                .vehiculeMarque(vehicule != null ? vehicule.getMarque() : null)
                .vehiculeModele(vehicule != null ? vehicule.getModele() : null)
                .build();
    }
}

