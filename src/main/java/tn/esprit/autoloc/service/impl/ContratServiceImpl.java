package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.PaiementResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;

    @Override
    public Contrat createContrat(Contrat contrat) {
        if (contrat == null) {
            throw new BusinessException("Les données du contrat ne peuvent pas être nulles");
        }
        if (contrat.getIdContrat() != null) {
            throw new BusinessException("L'identifiant du contrat est généré automatiquement : il ne doit pas être fourni à la création");
        }
        if (contrat.getDateSignature() == null) {
            contrat.setDateSignature(LocalDate.now());
        }
        if (contrat.getMontantTotal() != null && contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Le montant total ne peut pas être négatif");
        }
        if (contrat.getPaiements() == null) {
            contrat.setPaiements(new ArrayList<>());
        }
        // Chaque paiement doit référencer son contrat (côté propriétaire de la relation)
        contrat.getPaiements().forEach(paiement -> paiement.setContrat(contrat));
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat updateContrat(Long id, Contrat updatedContrat) {
        if (updatedContrat == null) {
            throw new BusinessException("Les données de mise à jour ne peuvent pas être nulles");
        }
        Contrat existingContrat = getContratById(id);

        if (updatedContrat.getDateSignature() != null) {
            existingContrat.setDateSignature(updatedContrat.getDateSignature());
        }
        if (updatedContrat.getMontantTotal() != null) {
            if (updatedContrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Le montant total ne peut pas être négatif");
            }
            existingContrat.setMontantTotal(updatedContrat.getMontantTotal());
        }
        existingContrat.setValide(updatedContrat.isValide());

        if (updatedContrat.getReservation() != null) {
            existingContrat.setReservation(updatedContrat.getReservation());
        }

        return contratRepository.save(existingContrat);
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat getContratById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> getAllContrats() {
        return contratRepository.findAll();
    }

    @Override
    public void deleteContrat(Long id) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Impossible de supprimer : aucun contrat avec l'identifiant : " + id));
        // Détacher la réservation (cascade ALL côté Reservation) pour qu'elle ne re-persiste pas le contrat supprimé
        if (contrat.getReservation() != null) {
            contrat.getReservation().setContrat(null);
        }
        contratRepository.delete(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat getContratByReservation(Long idReservation) {
        return contratRepository.findByReservation_IdReservation(idReservation)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun contrat associé à la réservation : " + idReservation));
    }

    @Override
    public Contrat validerContrat(Long idContrat) {
        Contrat contrat = getContratById(idContrat);
        contrat.setValide(true);
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat ajouterPaiement(Long idContrat, Paiement paiement) {
        if (paiement == null) {
            throw new BusinessException("Le paiement ne peut pas être nul");
        }
        Contrat contrat = getContratById(idContrat);
        paiement.setContrat(contrat);

        if (contrat.getPaiements() == null) {
            contrat.setPaiements(new ArrayList<>());
        }
        contrat.getPaiements().add(paiement);
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat retirerPaiement(Long idContrat, Long idPaiement) {
        Contrat contrat = getContratById(idContrat);
        if (contrat.getPaiements() != null) {
            contrat.getPaiements().removeIf(p -> p.getIdPaiement() != null && p.getIdPaiement().equals(idPaiement));
        }
        return contratRepository.save(contrat);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Service d'affectation
    // ═════════════════════════════════════════════════════════════════════════

    @Override
    public ContratDTO affecterContratAReservation(LocalDate dateSignature,
                                                   LocalDate dateDebut,
                                                   LocalDate dateFin) {
        // 1. Chercher une réservation EN_ATTENTE correspondant aux dates
        List<Reservation> candidates = reservationRepository
                .findByDateDebutAndDateFinAndStatut(dateDebut, dateFin, StatutReservation.EN_ATTENTE);
        if (candidates.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Aucune réservation EN_ATTENTE trouvée pour les dates " + dateDebut + " → " + dateFin);
        }
        Reservation reservation = candidates.get(0);

        // 2. Vérifier qu'aucun contrat n'existe déjà pour cette réservation
        if (contratRepository.findByReservation_IdReservation(
                reservation.getIdReservation()).isPresent()) {
            throw new BusinessException("Un contrat existe déjà pour cette réservation");
        }

        // 3. Calculer le montant total
        BigDecimal montant = BigDecimal.ZERO;
        if (reservation.getVehicule() != null
                && reservation.getVehicule().getTarifJournalier() != null) {
            long nbJours = ChronoUnit.DAYS.between(dateDebut, dateFin);
            if (nbJours < 1) nbJours = 1;
            montant = reservation.getVehicule().getTarifJournalier()
                    .multiply(BigDecimal.valueOf(nbJours));
        }

        // 4. Créer et persister le contrat
        Contrat contrat = new Contrat();
        contrat.setDateSignature(dateSignature != null ? dateSignature : LocalDate.now());
        contrat.setMontantTotal(montant);
        contrat.setValide(false);
        contrat.setReservation(reservation);
        contrat.setPaiements(new ArrayList<>());
        Contrat saved = contratRepository.save(contrat);

        // 5. Confirmer la réservation
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservationRepository.save(reservation);

        return toContratDTO(saved);
    }

    // ── Helper de mapping ─────────────────────────────────────────────────────

    private ContratDTO toContratDTO(Contrat c) {
        Reservation r  = c.getReservation();
        Client      cl = r != null ? r.getClient()   : null;
        Vehicule    v  = r != null ? r.getVehicule() : null;

        List<PaiementResponse> paiements = c.getPaiements() == null ? Collections.emptyList()
                : c.getPaiements().stream()
                        .map(p -> PaiementResponse.builder()
                                .idPaiement(p.getIdPaiement())
                                .montant(p.getMontant())
                                .datePaiement(p.getDatePaiement())
                                .modePaiement(p.getModePaiement())
                                .contratId(c.getIdContrat())
                                .build())
                        .collect(Collectors.toList());

        return ContratDTO.builder()
                .idContrat(c.getIdContrat())
                .dateSignature(c.getDateSignature())
                .montantTotal(c.getMontantTotal())
                .valide(c.isValide())
                .reservationId(r  != null ? r.getIdReservation()   : null)
                .reservationDateDebut(r != null ? r.getDateDebut() : null)
                .reservationDateFin(r   != null ? r.getDateFin()   : null)
                .clientId(cl != null ? cl.getIdClient()            : null)
                .clientNomComplet(cl != null
                        ? cl.getPrenom() + " " + cl.getNom()       : null)
                .vehiculeId(v != null ? v.getIdVehicule()          : null)
                .vehiculeImmatriculation(v != null ? v.getImmatriculation() : null)
                .paiements(paiements)
                .build();
    }
}
