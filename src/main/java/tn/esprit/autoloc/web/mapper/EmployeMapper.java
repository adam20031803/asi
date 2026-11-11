package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.web.dto.EmployeRequest;
import tn.esprit.autoloc.web.dto.EmployeResponse;

/**
 * Mapper manuel entre l'entité {@link Employe} et ses DTO.
 */
@Component
@RequiredArgsConstructor
public class EmployeMapper {

    private final IAgenceRepository agenceRepository;

    public Employe toEntity(EmployeRequest dto) {
        Employe employe = new Employe();
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setRole(dto.getRole());

        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId()).orElse(null);
            employe.setAgence(agence);
        }
        return employe;
    }

    public EmployeResponse toResponse(Employe employe) {
        return EmployeResponse.builder()
                .idEmploye(employe.getIdEmploye())
                .nom(employe.getNom())
                .prenom(employe.getPrenom())
                .role(employe.getRole())
                .agenceId(employe.getAgence() != null ? employe.getAgence().getIdAgence() : null)
                .agenceNom(employe.getAgence() != null ? employe.getAgence().getNom() : null)
                .build();
    }
}

