package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de création / mise à jour d'un client.
 */
@Data
public class ClientRequest {

    @NotBlank(message = "Le nom du client est obligatoire")
    @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom du client est obligatoire")
    @Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères")
    private String prenom;

    @NotBlank(message = "L'email du client est obligatoire")
    @Email(message = "L'adresse email est invalide")
    @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^[0-9+\\-\\s()]{6,20}$",
             message = "Le numéro de téléphone est invalide (6–20 chiffres, +, -, espaces ou parenthèses)")
    private String telephone;

    @NotBlank(message = "Le numéro de permis est obligatoire")
    @Size(max = 30, message = "Le numéro de permis ne peut pas dépasser 30 caractères")
    private String numPermis;
}

