package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequest;
import tn.esprit.autoloc.web.dto.ReservationResponse;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Réservations", description = "Gestion des réservations de véhicules")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @Operation(summary = "Lister les réservations")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<ReservationResponse> findAll() {
        return reservationService.getAllReservations().stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @GetMapping("/{id}")
    public ReservationResponse findById(
            @Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return reservationMapper.toResponse(reservationService.getReservationById(id));
    }

    @Operation(summary = "Créer une réservation",
               description = "L'identifiant est généré automatiquement. Le statut par défaut est EN_ATTENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides (date de fin antérieure à la date de début, champs manquants)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody ReservationRequest dto) {
        Reservation reservation = reservationMapper.toEntity(dto);
        return reservationMapper.toResponse(reservationService.createReservation(reservation));
    }

    @Operation(summary = "Modifier une réservation",
               description = "Seuls les champs non nuls du corps sont recopiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation modifiée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @PutMapping("/{id}")
    public ReservationResponse update(
            @Parameter(description = "Identifiant de la réservation") @PathVariable Long id,
            @Valid @RequestBody ReservationRequest dto) {
        Reservation reservation = reservationMapper.toEntity(dto);
        return reservationMapper.toResponse(reservationService.updateReservation(id, reservation));
    }

    @Operation(summary = "Supprimer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réservation supprimée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
