package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.MaintenanceRequest;
import tn.esprit.autoloc.web.dto.MaintenanceResponse;

/**
 * Mapper manuel entre l'entité {@link Maintenance} et ses DTO.
 */
@Component
@RequiredArgsConstructor
public class MaintenanceMapper {

    private final IVehiculeRepository vehiculeRepository;

    public Maintenance toEntity(MaintenanceRequest dto) {
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(dto.getDateDebut());
        maintenance.setDateFin(dto.getDateFin());
        maintenance.setDescription(dto.getDescription());

        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'identifiant : " + dto.getVehiculeId()));
        maintenance.setVehicule(vehicule);
        return maintenance;
    }

    public MaintenanceResponse toResponse(Maintenance maintenance) {
        Vehicule vehicule = maintenance.getVehicule();
        return MaintenanceResponse.builder()
                .idMaintenance(maintenance.getIdMaintenance())
                .dateDebut(maintenance.getDateDebut())
                .dateFin(maintenance.getDateFin())
                .description(maintenance.getDescription())
                .vehiculeId(vehicule != null ? vehicule.getIdVehicule() : null)
                .vehiculeImmatriculation(vehicule != null ? vehicule.getImmatriculation() : null)
                .vehiculeMarque(vehicule != null ? vehicule.getMarque() : null)
                .vehiculeModele(vehicule != null ? vehicule.getModele() : null)
                .build();
    }
}

