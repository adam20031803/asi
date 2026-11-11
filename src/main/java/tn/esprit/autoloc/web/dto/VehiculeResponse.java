package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de lecture d'un véhicule.
 */
@Data
@Builder
public class VehiculeResponse {

    private Long idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private CategorieVehicule categorie;
    private BigDecimal tarifJournalier;
    private StatutVehicule statut;

    /** Nom de l'agence (résumé, sans exposer l'entité complète). */
    private Long agenceId;
    private String agenceNom;

    /** Libellés des équipements affectés à ce véhicule. */
    private List<String> equipements;
}

