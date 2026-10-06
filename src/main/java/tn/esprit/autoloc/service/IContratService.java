package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;

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
}
