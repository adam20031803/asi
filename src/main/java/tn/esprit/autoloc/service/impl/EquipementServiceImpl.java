package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    public Equipement createEquipement(Equipement equipement) {
        if (equipement == null) {
            throw new BusinessException("Les données de l'équipement ne peuvent pas être nulles");
        }
        if (equipement.getLibelle() != null && equipementRepository.existsByLibelleIgnoreCase(equipement.getLibelle())) {
            throw new BusinessException("Un équipement avec le libellé '" + equipement.getLibelle() + "' existe déjà");
        }
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement updateEquipement(Long id, Equipement updatedEquipement) {
        if (updatedEquipement == null) {
            throw new BusinessException("Les données de mise à jour ne peuvent pas être nulles");
        }
        Equipement existingEquipement = getEquipementById(id);

        if (updatedEquipement.getLibelle() != null &&
                !updatedEquipement.getLibelle().equalsIgnoreCase(existingEquipement.getLibelle())) {
            if (equipementRepository.existsByLibelleIgnoreCase(updatedEquipement.getLibelle())) {
                throw new BusinessException("Le libellé '" + updatedEquipement.getLibelle() + "' existe déjà");
            }
            existingEquipement.setLibelle(updatedEquipement.getLibelle());
        }

        return equipementRepository.save(existingEquipement);
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement getEquipementById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> getAllEquipements() {
        return equipementRepository.findAll();
    }

    @Override
    public void deleteEquipement(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucun équipement avec l'identifiant : " + id);
        }
        equipementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement getEquipementByLibelle(String libelle) {
        return equipementRepository.findByLibelleIgnoreCase(libelle)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun équipement trouvé avec le libellé : " + libelle));
    }
}
