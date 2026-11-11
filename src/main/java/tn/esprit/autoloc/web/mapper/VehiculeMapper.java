package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.web.dto.VehiculeRequest;
import tn.esprit.autoloc.web.dto.VehiculeResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper manuel entre l'entité {@link Vehicule} et ses DTO.
 */
@Component
@RequiredArgsConstructor
public class VehiculeMapper {

    private final IAgenceRepository agenceRepository;

    public Vehicule toEntity(VehiculeRequest dto) {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setCategorie(dto.getCategorie());
        vehicule.setTarifJournalier(dto.getTarifJournalier());
        vehicule.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutVehicule.DISPONIBLE);

        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId()).orElse(null);
            vehicule.setAgence(agence);
        }
        return vehicule;
    }

    public VehiculeResponse toResponse(Vehicule vehicule) {
        List<String> equipements = vehicule.getEquipements() == null
                ? Collections.emptyList()
                : vehicule.getEquipements().stream()
                        .map(Equipement::getLibelle)
                        .collect(Collectors.toList());

        return VehiculeResponse.builder()
                .idVehicule(vehicule.getIdVehicule())
                .immatriculation(vehicule.getImmatriculation())
                .marque(vehicule.getMarque())
                .modele(vehicule.getModele())
                .categorie(vehicule.getCategorie())
                .tarifJournalier(vehicule.getTarifJournalier())
                .statut(vehicule.getStatut())
                .agenceId(vehicule.getAgence() != null ? vehicule.getAgence().getIdAgence() : null)
                .agenceNom(vehicule.getAgence() != null ? vehicule.getAgence().getNom() : null)
                .equipements(equipements)
                .build();
    }
}

