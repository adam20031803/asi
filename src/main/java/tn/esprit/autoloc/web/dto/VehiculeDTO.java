package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de réponse pour les services d'affectation de véhicule
 * (affectation d'équipement, affectation de maintenance).
 */
@Data
@Builder
public class VehiculeDTO {

    private Long idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private CategorieVehicule categorie;
    private BigDecimal tarifJournalier;
    private StatutVehicule statut;

    // Agence de rattachement
    private Long agenceId;
    private String agenceNom;

    // Équipements affectés
    private List<String> equipements;

    // Nombre de maintenances enregistrées
    private int nombreMaintenances;
}

