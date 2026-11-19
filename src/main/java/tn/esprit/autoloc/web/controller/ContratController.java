package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.ContratRequest;
import tn.esprit.autoloc.web.dto.ContratResponse;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Contrats", description = "Gestion des contrats de location")
@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
public class ContratController {

    private final IContratService contratService;
    private final ContratMapper contratMapper;

    @Operation(summary = "Lister les contrats")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<ContratResponse> findAll() {
        return contratService.getAllContrats().stream()
                .map(contratMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @GetMapping("/{id}")
    public ContratResponse findById(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        return contratMapper.toResponse(contratService.getContratById(id));
    }

    @Operation(summary = "Créer un contrat",
               description = "L'identifiant est généré automatiquement. La date de signature est fixée à aujourd'hui si absente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides (montant négatif, réservation introuvable)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContratResponse create(@Valid @RequestBody ContratRequest dto) {
        Contrat contrat = contratMapper.toEntity(dto);
        return contratMapper.toResponse(contratService.createContrat(contrat));
    }

    @Operation(summary = "Modifier un contrat",
               description = "Seuls les champs non nuls du corps sont recopiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @PutMapping("/{id}")
    public ContratResponse update(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id,
            @Valid @RequestBody ContratRequest dto) {
        Contrat contrat = contratMapper.toEntity(dto);
        return contratMapper.toResponse(contratService.updateContrat(id, contrat));
    }

    @Operation(summary = "Supprimer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        contratService.deleteContrat(id);
    }

    // ── Service d'affectation ──────────────────────────────────────────────

    @Operation(summary = "Créer un contrat et l'affecter à une réservation EN_ATTENTE",
               description = "Recherche la première réservation EN_ATTENTE dont les dates correspondent, crée le contrat et passe la réservation en CONFIRMEE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé et affecté"),
            @ApiResponse(responseCode = "400", description = "Un contrat existe déjà pour cette réservation"),
            @ApiResponse(responseCode = "404", description = "Aucune réservation EN_ATTENTE trouvée pour ces dates")
    })
    @PostMapping("/affecter-reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public ContratDTO affecterContratAReservation(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSignature,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return contratService.affecterContratAReservation(dateSignature, dateDebut, dateFin);
    }
}
