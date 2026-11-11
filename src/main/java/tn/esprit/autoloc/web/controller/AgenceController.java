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
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceRequest;
import tn.esprit.autoloc.web.dto.AgenceResponse;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Agences", description = "Gestion des agences de location")
@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;
    private final AgenceMapper agenceMapper;

    @Operation(summary = "Lister les agences")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<AgenceResponse> findAll() {
        return agenceService.getAllAgences().stream()
                .map(agenceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence trouvée"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @GetMapping("/{id}")
    public AgenceResponse findById(
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long id) {
        return agenceMapper.toResponse(agenceService.getAgenceById(id));
    }

    @Operation(summary = "Créer une agence",
               description = "L'identifiant est généré automatiquement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agence créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenceResponse create(@Valid @RequestBody AgenceRequest dto) {
        Agence agence = agenceMapper.toEntity(dto);
        return agenceMapper.toResponse(agenceService.createAgence(agence));
    }

    @Operation(summary = "Modifier une agence",
               description = "Seuls les champs non nuls du corps sont recopiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence modifiée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @PutMapping("/{id}")
    public AgenceResponse update(
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long id,
            @Valid @RequestBody AgenceRequest dto) {
        Agence agence = agenceMapper.toEntity(dto);
        return agenceMapper.toResponse(agenceService.updateAgence(id, agence));
    }

    @Operation(summary = "Supprimer une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agence supprimée"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long id) {
        agenceService.deleteAgence(id);
    }
}
