package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de création / mise à jour d'une agence.
 * Les contraintes de validation sont appliquées avant l'entrée en service.
 */
@Data
public class AgenceRequest {

    @NotBlank(message = "Le nom de l'agence est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String nom;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 50, message = "La ville ne peut pas dépasser 50 caractères")
    private String ville;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 150, message = "L'adresse ne peut pas dépasser 150 caractères")
    private String adresse;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9+\\-\\s()]{6,20}$",
             message = "Le numéro de téléphone est invalide (6–20 chiffres, +, -, espaces ou parenthèses)")
    private String telephone;
}

