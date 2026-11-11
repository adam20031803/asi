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
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeRequest;
import tn.esprit.autoloc.web.dto.EmployeResponse;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Employés", description = "Gestion des employés des agences")
@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final IEmployeService employeService;
    private final EmployeMapper employeMapper;

    @Operation(summary = "Lister les employés")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<EmployeResponse> findAll() {
        return employeService.getAllEmployes().stream()
                .map(employeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @GetMapping("/{id}")
    public EmployeResponse findById(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        return employeMapper.toResponse(employeService.getEmployeById(id));
    }

    @Operation(summary = "Créer un employé",
               description = "L'identifiant est généré automatiquement.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employé créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides (rôle inconnu, champ obligatoire manquant)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeResponse create(@Valid @RequestBody EmployeRequest dto) {
        Employe employe = employeMapper.toEntity(dto);
        return employeMapper.toResponse(employeService.createEmploye(employe));
    }

    @Operation(summary = "Modifier un employé",
               description = "Seuls les champs non nuls du corps sont recopiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @PutMapping("/{id}")
    public EmployeResponse update(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id,
            @Valid @RequestBody EmployeRequest dto) {
        Employe employe = employeMapper.toEntity(dto);
        return employeMapper.toResponse(employeService.updateEmploye(id, employe));
    }

    @Operation(summary = "Supprimer un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employé supprimé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        employeService.deleteEmploye(id);
    }
}
