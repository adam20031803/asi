package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tn.esprit.autoloc.domain.RoleEmploye;

/**
 * DTO de création / mise à jour d'un employé.
 */
@Data
public class EmployeRequest {

    @NotBlank(message = "Le nom de l'employé est obligatoire")
    @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom de l'employé est obligatoire")
    @Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères")
    private String prenom;

    @NotNull(message = "Le rôle de l'employé est obligatoire")
    private RoleEmploye role;

    /** Identifiant de l'agence à laquelle rattacher l'employé. */
    private Long agenceId;
}

