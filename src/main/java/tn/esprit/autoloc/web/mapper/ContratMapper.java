package tn.esprit.autoloc.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.web.dto.ContratRequest;
import tn.esprit.autoloc.web.dto.ContratResponse;
import tn.esprit.autoloc.web.dto.PaiementResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper manuel entre l'entité {@link Contrat} et ses DTO.
 */
@Component
@RequiredArgsConstructor
public class ContratMapper {

    private final IReservationRepository reservationRepository;
    private final PaiementMapper paiementMapper;

    public Contrat toEntity(ContratRequest dto) {
        Contrat contrat = new Contrat();
        contrat.setDateSignature(dto.getDateSignature() != null ? dto.getDateSignature() : LocalDate.now());
        contrat.setMontantTotal(dto.getMontantTotal());
        contrat.setValide(dto.isValide());
        contrat.setPaiements(new ArrayList<>());

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable avec l'identifiant : " + dto.getReservationId()));
        contrat.setReservation(reservation);

        return contrat;
    }

    public ContratResponse toResponse(Contrat contrat) {
        List<PaiementResponse> paiements = contrat.getPaiements() == null
                ? Collections.emptyList()
                : contrat.getPaiements().stream()
                        .map(paiementMapper::toResponse)
                        .collect(Collectors.toList());

        return ContratResponse.builder()
                .idContrat(contrat.getIdContrat())
                .dateSignature(contrat.getDateSignature())
                .montantTotal(contrat.getMontantTotal())
                .valide(contrat.isValide())
                .reservationId(contrat.getReservation() != null ? contrat.getReservation().getIdReservation() : null)
                .paiements(paiements)
                .build();
    }
}

