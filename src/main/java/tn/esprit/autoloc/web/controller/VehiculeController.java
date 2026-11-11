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
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeRequest;
import tn.esprit.autoloc.web.dto.VehiculeResponse;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Véhicules", description = "Gestion du parc de véhicules")
@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    @Operation(summary = "Lister les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<VehiculeResponse> findAll() {
        return vehiculeService.getAllVehicules().stream()
                .map(vehiculeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @GetMapping("/{id}")
    public VehiculeResponse findById(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return vehiculeMapper.toResponse(vehiculeService.getVehiculeById(id));
    }

    @Operation(summary = "Créer un véhicule",
               description = "L'identifiant est généré automatiquement. Le statut par défaut est DISPONIBLE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides (tarif invalide, immatriculation déjà utilisée)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeResponse create(@Valid @RequestBody VehiculeRequest dto) {
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        return vehiculeMapper.toResponse(vehiculeService.createVehicule(vehicule));
    }

    @Operation(summary = "Modifier un véhicule",
               description = "Seuls les champs non nuls du corps sont recopiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @PutMapping("/{id}")
    public VehiculeResponse update(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
            @Valid @RequestBody VehiculeRequest dto) {
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        return vehiculeMapper.toResponse(vehiculeService.updateVehicule(id, vehicule));
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteVehicule(id);
    }
}
