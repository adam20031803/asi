package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.IContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    public Contrat createContrat(Contrat contrat) {
        if (contrat == null) {
            throw new BusinessException("Les données du contrat ne peuvent pas être nulles");
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
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucun contrat avec l'identifiant : " + id);
        }
        contratRepository.deleteById(id);
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
}
