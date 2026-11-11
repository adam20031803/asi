package tn.esprit.autoloc.web.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de lecture d'un équipement.
 */
@Data
@Builder
public class EquipementResponse {

    private Long idEquipement;
    private String libelle;
}

