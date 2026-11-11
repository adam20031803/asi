package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementRequest;
import tn.esprit.autoloc.web.dto.EquipementResponse;

/**
 * Mapper manuel entre l'entité {@link Equipement} et ses DTO.
 */
@Component
public class EquipementMapper {

    public Equipement toEntity(EquipementRequest dto) {
        Equipement equipement = new Equipement();
        equipement.setLibelle(dto.getLibelle());
        return equipement;
    }

    public EquipementResponse toResponse(Equipement equipement) {
        return EquipementResponse.builder()
                .idEquipement(equipement.getIdEquipement())
                .libelle(equipement.getLibelle())
                .build();
    }
}

