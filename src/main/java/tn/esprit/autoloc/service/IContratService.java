package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.ContratDTO;

import java.time.LocalDate;
import java.util.List;

public interface IContratService {

    Contrat createContrat(Contrat contrat);

    Contrat updateContrat(Long id, Contrat contrat);

    Contrat getContratById(Long id);

    List<Contrat> getAllContrats();

    void deleteContrat(Long id);

    Contrat getContratByReservation(Long idReservation);

    Contrat validerContrat(Long idContrat);

    Contrat ajouterPaiement(Long idContrat, Paiement paiement);

    Contrat retirerPaiement(Long idContrat, Long idPaiement);

    // ── Service d'affectation ────────────────────────────────────────────────

    /**
     * Crée un contrat et l'affecte à la première réservation EN_ATTENTE dont
     * les dates de début et de fin correspondent aux paramètres fournis.
     *
     * @param dateSignature date de signature du contrat (aujourd'hui si null)
     * @param dateDebut     date de début de la réservation recherchée
     * @param dateFin       date de fin  de la réservation recherchée
     * @return ContratDTO du contrat créé et affecté
     */
    ContratDTO affecterContratAReservation(LocalDate dateSignature,
                                           LocalDate dateDebut,
                                           LocalDate dateFin);
}
