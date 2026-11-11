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
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceRequest;
import tn.esprit.autoloc.web.dto.MaintenanceResponse;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Maintenances", description = "Gestion des maintenances des véhicules")
@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;
    private final MaintenanceMapper maintenanceMapper;

    @Operation(summary = "Lister les maintenances")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<MaintenanceResponse> findAll() {
        return maintenanceService.getAllMaintenances().stream()
                .map(maintenanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @GetMapping("/{id}")
    public MaintenanceResponse findById(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        return maintenanceMapper.toResponse(maintenanceService.getMaintenanceById(id));
    }

    @Operation(summary = "Créer une maintenance",
               description = "L'identifiant est généré automatiquement. Le véhicule passe automatiquement au statut MAINTENANCE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Maintenance créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides (date de fin antérieure à la date de début, véhicule introuvable)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceResponse create(@Valid @RequestBody MaintenanceRequest dto) {
        Maintenance maintenance = maintenanceMapper.toEntity(dto);
        return maintenanceMapper.toResponse(maintenanceService.createMaintenance(maintenance));
    }

    @Operation(summary = "Modifier une maintenance",
               description = "Seuls les champs non nuls du corps sont recopiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance modifiée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @PutMapping("/{id}")
    public MaintenanceResponse update(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id,
            @Valid @RequestBody MaintenanceRequest dto) {
        Maintenance maintenance = maintenanceMapper.toEntity(dto);
        return maintenanceMapper.toResponse(maintenanceService.updateMaintenance(id, maintenance));
    }

    @Operation(summary = "Supprimer une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Maintenance supprimée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
    }
}
