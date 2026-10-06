package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;

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
}
