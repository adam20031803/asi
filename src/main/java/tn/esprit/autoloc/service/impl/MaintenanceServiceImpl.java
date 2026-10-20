package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.BusinessException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IMaintenanceService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Maintenance createMaintenance(Maintenance maintenance) {
        if (maintenance == null) {
            throw new BusinessException("Les données de la maintenance ne peuvent pas être nulles");
        }
        if (maintenance.getDateDebut() == null || maintenance.getDateFin() == null) {
            throw new BusinessException("Les dates de début et de fin de maintenance sont obligatoires");
        }
        if (maintenance.getDateFin().isBefore(maintenance.getDateDebut())) {
            throw new BusinessException("La date de fin ne peut pas être antérieure à la date de début");
        }
        if (maintenance.getIdMaintenance() != null) {
            throw new BusinessException("L'identifiant de la maintenance est généré automatiquement : il ne doit pas être fourni à la création");
        }
        if (maintenance.getVehicule() != null) {
            // Le corps ne contient que l'identifiant du véhicule : on charge le véhicule complet avant de le modifier
            Vehicule vehicule = findVehicule(maintenance.getVehicule().getIdVehicule());
            vehicule.setStatut(StatutVehicule.MAINTENANCE);
            maintenance.setVehicule(vehicule);
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance updateMaintenance(Long id, Maintenance updatedMaintenance) {
        if (updatedMaintenance == null) {
            throw new BusinessException("Les données de mise à jour ne peuvent pas être nulles");
        }
        Maintenance existingMaintenance = getMaintenanceById(id);

        if (updatedMaintenance.getDateDebut() != null && updatedMaintenance.getDateFin() != null) {
            if (updatedMaintenance.getDateFin().isBefore(updatedMaintenance.getDateDebut())) {
                throw new BusinessException("La date de fin ne peut pas être antérieure à la date de début");
            }
            existingMaintenance.setDateDebut(updatedMaintenance.getDateDebut());
            existingMaintenance.setDateFin(updatedMaintenance.getDateFin());
        }

        if (updatedMaintenance.getDescription() != null) {
            existingMaintenance.setDescription(updatedMaintenance.getDescription());
        }
        if (updatedMaintenance.getVehicule() != null) {
            existingMaintenance.setVehicule(findVehicule(updatedMaintenance.getVehicule().getIdVehicule()));
        }

        return maintenanceRepository.save(existingMaintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance introuvable avec l'identifiant : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    @Override
    public void deleteMaintenance(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : aucune maintenance avec l'identifiant : " + id);
        }
        maintenanceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> getMaintenancesByVehicule(Long idVehicule) {
        return maintenanceRepository.findByVehicule_IdVehicule(idVehicule);
    }

    private Vehicule findVehicule(Long idVehicule) {
        if (idVehicule == null) {
            throw new BusinessException("L'identifiant du véhicule est obligatoire");
        }
        return vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'identifiant : " + idVehicule));
    }
}
