package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IVehiculeService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IEquipementRepository equipementRepository;

    @Override
    public Vehicule createVehicule(Vehicule vehicule) {
        if (vehicule == null) {
            throw new BusinessException("Les données du véhicule ne peuvent pas être nulles");
        }
        if (vehicule.getIdVehicule() != null) {
            throw new BusinessException("L'identifiant du véhicule est généré automatiquement : il ne doit pas être fourni à la création");
        }
        if (vehicule.getImmatriculation() != null && vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new BusinessException("Un véhicule avec l'immatriculation '" + vehicule.getImmatriculation() + "' existe déjà");
        }
        if (vehicule.getTarifJournalier() != null && vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Le tarif journalier doit être strictement supérieur à zéro");
        }
        if (vehicule.getStatut() == null) {
            vehicule.setStatut(StatutVehicule.DISPONIBLE);
        }
        if (vehicule.getEquipements() == null) {
            vehicule.setEquipements(new ArrayList<>());
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule updateVehicule(Long id, Vehicule updatedVehicule) {
        if (updatedVehicule == null) {
            throw new BusinessException("Les données de mise à jour du véhicule ne peuvent pas être nulles");
        }
        Vehicule existingVehicule = getVehiculeById(id);

        if (updatedVehicule.getImmatriculation() != null &&
                !updatedVehicule.getImmatriculation().equalsIgnoreCase(existingVehicule.getImmatriculation())) {
            if (vehiculeRepository.existsByImmatriculation(updatedVehicule.getImmatriculation())) {
                throw new BusinessException("L'immatriculation '" + updatedVehicule.getImmatriculation() + "' est déjà utilisée par un autre véhicule");
            }
            existingVehicule.setImmatriculation(updatedVehicule.getImmatriculation());
        }

        if (updatedVehicule.getMarque() != null) {
            existingVehicule.setMarque(updatedVehicule.getMarque());
        }
        if (updatedVehicule.getModele() != null) {
            existingVehicule.setModele(updatedVehicule.getModele());
        }
        if (updatedVehicule.getCategorie() != null) {
            existingVehicule.setCategorie(updatedVehicule.getCategorie());
        }
        if (updatedVehicule.getTarifJournalier() != null) {
            if (updatedVehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Le tarif journalier doit être strictement positif");
            }
            existingVehicule.setTarifJournalier(updatedVehicule.getTarifJournalier());
        }
        if (updatedVehicule.getStatut() != null) {
            existingVehicule.setStatut(updatedVehicule.getStatut());
        }
        if (updatedVehicule.getAgence() != null) {
            existingVehicule.setAgence(updatedVehicule.getAgence());
        }

        return vehiculeRepository.save(existingVehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule getVehiculeById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    @Override
    public void deleteVehicule(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucun véhicule avec l'identifiant : " + id);
        }
        vehiculeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule getVehiculeByImmatriculation(String immatriculation) {
        return vehiculeRepository.findByImmatriculation(immatriculation)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun véhicule trouvé avec l'immatriculation : " + immatriculation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> getVehiculesByStatut(StatutVehicule statut) {
        return vehiculeRepository.findByStatut(statut);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> getVehiculesByCategorie(CategorieVehicule categorie) {
        return vehiculeRepository.findByCategorie(categorie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> getVehiculesByAgence(Long idAgence) {
        return vehiculeRepository.findByAgence_IdAgence(idAgence);
    }

    @Override
    public Vehicule changerStatut(Long idVehicule, StatutVehicule nouveauStatut) {
        Vehicule vehicule = getVehiculeById(idVehicule);
        vehicule.setStatut(nouveauStatut);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = getVehiculeById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement introuvable avec l'identifiant : " + idEquipement));

        if (vehicule.getEquipements() == null) {
            vehicule.setEquipements(new ArrayList<>());
        }
        if (!vehicule.getEquipements().contains(equipement)) {
            vehicule.getEquipements().add(equipement);
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule retirerEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = getVehiculeById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement introuvable avec l'identifiant : " + idEquipement));

        if (vehicule.getEquipements() != null) {
            vehicule.getEquipements().remove(equipement);
        }
        return vehiculeRepository.save(vehicule);
    }
}
