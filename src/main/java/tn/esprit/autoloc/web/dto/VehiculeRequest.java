package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

import java.math.BigDecimal;

/**
 * DTO de création / mise à jour d'un véhicule.
 */
@Data
public class VehiculeRequest {

    @NotBlank(message = "L'immatriculation est obligatoire")
    @Pattern(regexp = "^[A-Z0-9\\-]{2,20}$",
             message = "L'immatriculation ne peut contenir que des lettres majuscules, chiffres et tirets (2–20 caractères)")
    private String immatriculation;

    @NotBlank(message = "La marque est obligatoire")
    @Size(max = 50, message = "La marque ne peut pas dépasser 50 caractères")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    @Size(max = 50, message = "Le modèle ne peut pas dépasser 50 caractères")
    private String modele;

    @NotNull(message = "La catégorie du véhicule est obligatoire")
    private CategorieVehicule categorie;

    @NotNull(message = "Le tarif journalier est obligatoire")
    @DecimalMin(value = "0.01", message = "Le tarif journalier doit être strictement supérieur à zéro")
    private BigDecimal tarifJournalier;

    private StatutVehicule statut;

    /** Identifiant de l'agence à laquelle rattacher le véhicule (optionnel). */
    private Long agenceId;
}

