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
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementRequest;
import tn.esprit.autoloc.web.dto.EquipementResponse;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Équipements", description = "Gestion du catalogue d'équipements")
@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final IEquipementService equipementService;
    private final EquipementMapper equipementMapper;

    @Operation(summary = "Lister les équipements")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<EquipementResponse> findAll() {
        return equipementService.getAllEquipements().stream()
                .map(equipementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    @GetMapping("/{id}")
    public EquipementResponse findById(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        return equipementMapper.toResponse(equipementService.getEquipementById(id));
    }

    @Operation(summary = "Créer un équipement",
               description = "L'identifiant est généré automatiquement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Équipement créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides (libellé vide ou déjà existant)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipementResponse create(@Valid @RequestBody EquipementRequest dto) {
        Equipement equipement = equipementMapper.toEntity(dto);
        return equipementMapper.toResponse(equipementService.createEquipement(equipement));
    }

    @Operation(summary = "Modifier un équipement",
               description = "Seuls les champs non nuls du corps sont recopiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    @PutMapping("/{id}")
    public EquipementResponse update(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id,
            @Valid @RequestBody EquipementRequest dto) {
        Equipement equipement = equipementMapper.toEntity(dto);
        return equipementMapper.toResponse(equipementService.updateEquipement(id, equipement));
    }

    @Operation(summary = "Supprimer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Équipement supprimé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        equipementService.deleteEquipement(id);
    }
}
