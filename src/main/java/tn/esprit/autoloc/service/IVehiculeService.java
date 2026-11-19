package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

import java.util.List;

public interface IVehiculeService {

    Vehicule createVehicule(Vehicule vehicule);

    Vehicule updateVehicule(Long id, Vehicule vehicule);

    Vehicule getVehiculeById(Long id);

    List<Vehicule> getAllVehicules();

    void deleteVehicule(Long id);

    Vehicule getVehiculeByImmatriculation(String immatriculation);

    List<Vehicule> getVehiculesByStatut(StatutVehicule statut);

    List<Vehicule> getVehiculesByCategorie(CategorieVehicule categorie);

    List<Vehicule> getVehiculesByAgence(Long idAgence);

    Vehicule changerStatut(Long idVehicule, StatutVehicule nouveauStatut);

    Vehicule affecterEquipement(Long idVehicule, Long idEquipement);

    Vehicule retirerEquipement(Long idVehicule, Long idEquipement);

    // ── Services d'affectation ───────────────────────────────────────────────

    /**
     * Affecte un équipement à un véhicule et retourne le VehiculeDTO mis à jour.
     */
    VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);

    /**
     * Crée une maintenance pour le véhicule identifié par son immatriculation,
     * met le véhicule en statut MAINTENANCE et retourne le VehiculeDTO mis à jour.
     */
    VehiculeDTO affecterMaintenanceAVehicule(String description, String immatriculation);

    /**
     * Alias respectant la casse exacte : affecterMaintenanceAvehicule.
     */
    default VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation) {
        return affecterMaintenanceAVehicule(description, immatriculation);
    }
}
