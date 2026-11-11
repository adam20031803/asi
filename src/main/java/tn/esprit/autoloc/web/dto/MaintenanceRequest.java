package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de création / mise à jour d'une maintenance.
 */
@Data
public class MaintenanceRequest {

    @NotNull(message = "La date de début de la maintenance est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin de la maintenance est obligatoire")
    private LocalDate dateFin;

    @NotBlank(message = "La description de la maintenance est obligatoire")
    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    private String description;

    @NotNull(message = "L'identifiant du véhicule est obligatoire")
    private Long vehiculeId;
}

