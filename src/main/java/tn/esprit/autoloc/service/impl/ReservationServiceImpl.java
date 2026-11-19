package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.*;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository    vehiculeRepository;
    private final IClientRepository      clientRepository;
    private final IContratRepository     contratRepository;

    // ═════════════════════════════════════════════════════════════════════════
    // CRUD de base (inchangé)
    // ═════════════════════════════════════════════════════════════════════════

    @Override
    public Reservation createReservation(Reservation reservation) {
        if (reservation == null) {
            throw new BusinessException("Les données de la réservation ne peuvent pas être nulles");
        }
        if (reservation.getIdReservation() != null) {
            throw new BusinessException("L'identifiant de la réservation est généré automatiquement");
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
        Reservation existing = getReservationById(id);

        if (updatedReservation.getDateDebut() != null && updatedReservation.getDateFin() != null) {
            if (updatedReservation.getDateFin().isBefore(updatedReservation.getDateDebut())) {
                throw new BusinessException("La date de fin ne peut pas être antérieure à la date de début");
            }
            existing.setDateDebut(updatedReservation.getDateDebut());
            existing.setDateFin(updatedReservation.getDateFin());
        }
        if (updatedReservation.getStatut() != null) {
            existing.setStatut(updatedReservation.getStatut());
        }
        if (updatedReservation.getClient() != null) {
            existing.setClient(updatedReservation.getClient());
        }
        if (updatedReservation.getVehicule() != null) {
            existing.setVehicule(updatedReservation.getVehicule());
        }
        return reservationRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Impossible de supprimer : aucune réservation avec l'identifiant : " + id);
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

    // ═════════════════════════════════════════════════════════════════════════
    // SERVICE 1 — affecterReservationAClient
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Affecte la réservation {idReservation} au premier client trouvé
     * portant le prénom et le nom fournis (insensible à la casse).
     * Lève ResourceNotFoundException si aucun client ou aucune réservation n'est trouvé.
     */
    @Override
    public ReservationResponseDTO affecterReservationAClient(Long idReservation,
                                                              String nom,
                                                              String prenom) {
        // 1. Récupérer la réservation
        Reservation reservation = getReservationById(idReservation);

        // 2. Trouver le client par nom + prénom
        List<Client> clients = clientRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom);
        if (clients.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Aucun client trouvé avec le nom '" + nom + "' et le prénom '" + prenom + "'");
        }
        Client client = clients.get(0);

        // 3. Affecter et persister
        reservation.setClient(client);
        Reservation saved = reservationRepository.save(reservation);

        return toResponseDTO(saved);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SERVICE 5 — creerReservationEtContratAssocie
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Crée simultanément une réservation et son contrat associé.
     * Le montant du contrat = tarifJournalier × nbJours si non fourni.
     */
    @Override
    public ReservationResponseDTO creerReservationEtContratAssocie(
            ReservationWithContratRequestDTO dto) {

        if (dto == null) {
            throw new BusinessException("Le DTO de création ne peut pas être nul");
        }
        if (dto.getDateFin() != null && dto.getDateDebut() != null
                && dto.getDateFin().isBefore(dto.getDateDebut())) {
            throw new BusinessException("La date de fin ne peut pas être antérieure à la date de début");
        }

        // 1. Résoudre client et véhicule
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client introuvable avec l'identifiant : " + dto.getClientId()));

        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'identifiant : " + dto.getVehiculeId()));

        // 2. Construire la réservation
        Reservation reservation = new Reservation();
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(dto.getStatutReservation() != null
                ? dto.getStatutReservation() : StatutReservation.CONFIRMEE);
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        Reservation savedReservation = reservationRepository.save(reservation);

        // 3. Calculer le montant si absent
        BigDecimal montant = dto.getMontantTotal();
        if (montant == null && vehicule.getTarifJournalier() != null) {
            long nbJours = ChronoUnit.DAYS.between(dto.getDateDebut(), dto.getDateFin());
            if (nbJours < 1) nbJours = 1;
            montant = vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(nbJours));
        }

        // 4. Construire le contrat
        Contrat contrat = new Contrat();
        contrat.setDateSignature(dto.getDateSignature() != null
                ? dto.getDateSignature() : LocalDate.now());
        contrat.setMontantTotal(montant != null ? montant : BigDecimal.ZERO);
        contrat.setValide(false);
        contrat.setReservation(savedReservation);
        contrat.setPaiements(new ArrayList<>());
        contratRepository.save(contrat);

        // 5. Marquer le véhicule comme LOUE
        vehicule.setStatut(StatutVehicule.LOUE);
        vehiculeRepository.save(vehicule);

        // 6. Retourner le DTO de réservation enrichi
        ReservationResponseDTO responseDTO = toResponseDTO(savedReservation);
        responseDTO.setContratId(contrat.getIdContrat());
        return responseDTO;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Méthode utilitaire de mapping interne
    // ═════════════════════════════════════════════════════════════════════════

    private ReservationResponseDTO toResponseDTO(Reservation r) {
        Client cl = r.getClient();
        Vehicule v = r.getVehicule();
        return ReservationResponseDTO.builder()
                .idReservation(r.getIdReservation())
                .dateDebut(r.getDateDebut())
                .dateFin(r.getDateFin())
                .statut(r.getStatut())
                .clientId(cl != null ? cl.getIdClient() : null)
                .clientNom(cl != null ? cl.getNom() : null)
                .clientPrenom(cl != null ? cl.getPrenom() : null)
                .clientNomComplet(cl != null ? cl.getPrenom() + " " + cl.getNom() : null)
                .vehiculeId(v != null ? v.getIdVehicule() : null)
                .vehiculeImmatriculation(v != null ? v.getImmatriculation() : null)
                .vehiculeMarque(v != null ? v.getMarque() : null)
                .vehiculeModele(v != null ? v.getModele() : null)
                .build();
    }
}
