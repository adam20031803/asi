package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.ModePaiement;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.service.IPaiementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement createPaiement(Paiement paiement) {
        if (paiement == null) {
            throw new BusinessException("Les données du paiement ne peuvent pas être nulles");
        }
        if (paiement.getMontant() == null || paiement.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Le montant du paiement doit être strictement positif");
        }
        if (paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDate.now());
        }
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement updatePaiement(Long id, Paiement updatedPaiement) {
        if (updatedPaiement == null) {
            throw new BusinessException("Les données de mise à jour ne peuvent pas être nulles");
        }
        Paiement existingPaiement = getPaiementById(id);

        if (updatedPaiement.getMontant() != null) {
            if (updatedPaiement.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Le montant doit être strictement positif");
            }
            existingPaiement.setMontant(updatedPaiement.getMontant());
        }
        if (updatedPaiement.getDatePaiement() != null) {
            existingPaiement.setDatePaiement(updatedPaiement.getDatePaiement());
        }
        if (updatedPaiement.getModePaiement() != null) {
            existingPaiement.setModePaiement(updatedPaiement.getModePaiement());
        }
        if (updatedPaiement.getContrat() != null) {
            existingPaiement.setContrat(updatedPaiement.getContrat());
        }

        return paiementRepository.save(existingPaiement);
    }

    @Override
    @Transactional(readOnly = true)
    public Paiement getPaiementById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    @Override
    public void deletePaiement(Long id) {
        if (!paiementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucun paiement avec l'identifiant : " + id);
        }
        paiementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> getPaiementsByContrat(Long idContrat) {
        return paiementRepository.findByContrat_IdContrat(idContrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> getPaiementsByModePaiement(ModePaiement modePaiement) {
        return paiementRepository.findByModePaiement(modePaiement);
    }
}
