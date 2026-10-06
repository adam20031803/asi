package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Equipement;

import java.util.List;

public interface IEquipementService {

    Equipement createEquipement(Equipement equipement);

    Equipement updateEquipement(Long id, Equipement equipement);

    Equipement getEquipementById(Long id);

    List<Equipement> getAllEquipements();

    void deleteEquipement(Long id);

    Equipement getEquipementByLibelle(String libelle);
}
