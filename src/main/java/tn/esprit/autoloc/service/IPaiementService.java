package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.ModePaiement;
import tn.esprit.autoloc.domain.Paiement;

import java.util.List;

public interface IPaiementService {

    Paiement createPaiement(Paiement paiement);

    Paiement updatePaiement(Long id, Paiement paiement);

    Paiement getPaiementById(Long id);

    List<Paiement> getAllPaiements();

    void deletePaiement(Long id);

    List<Paiement> getPaiementsByContrat(Long idContrat);

    List<Paiement> getPaiementsByModePaiement(ModePaiement modePaiement);
}
