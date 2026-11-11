package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de création / mise à jour d'un équipement.
 */
@Data
public class EquipementRequest {

    @NotBlank(message = "Le libellé de l'équipement est obligatoire")
    @Size(max = 50, message = "Le libellé ne peut pas dépasser 50 caractères")
    private String libelle;
}

