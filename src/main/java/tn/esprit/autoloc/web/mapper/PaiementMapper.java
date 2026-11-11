package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.web.dto.PaiementRequest;
import tn.esprit.autoloc.web.dto.PaiementResponse;

import java.time.LocalDate;

/**
 * Mapper manuel entre l'entité {@link Paiement} et ses DTO.
 */
@Component
@RequiredArgsConstructor
public class PaiementMapper {

    private final IContratRepository contratRepository;

    public Paiement toEntity(PaiementRequest dto) {
        Paiement paiement = new Paiement();
        paiement.setMontant(dto.getMontant());
        paiement.setDatePaiement(dto.getDatePaiement() != null ? dto.getDatePaiement() : LocalDate.now());
        paiement.setModePaiement(dto.getModePaiement());

        Contrat contrat = contratRepository.findById(dto.getContratId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrat introuvable avec l'identifiant : " + dto.getContratId()));
        paiement.setContrat(contrat);
        return paiement;
    }

    public PaiementResponse toResponse(Paiement paiement) {
        return PaiementResponse.builder()
                .idPaiement(paiement.getIdPaiement())
                .montant(paiement.getMontant())
                .datePaiement(paiement.getDatePaiement())
                .modePaiement(paiement.getModePaiement())
                .contratId(paiement.getContrat() != null ? paiement.getContrat().getIdContrat() : null)
                .build();
    }
}

