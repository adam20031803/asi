package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementResponse;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lecture seule : les paiements sont créés, modifiés et supprimés via leur contrat (cascade ALL).
 * Toute autre méthode HTTP sur /paiements renvoie 405 Method Not Allowed.
 */
@Tag(name = "Paiements", description = "Consultation des paiements (gérés par cascade depuis les contrats)")
@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final IPaiementService paiementService;
    private final PaiementMapper paiementMapper;

    @Operation(summary = "Lister les paiements")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<PaiementResponse> findAll() {
        return paiementService.getAllPaiements().stream()
                .map(paiementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un paiement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    @GetMapping("/{id}")
    public PaiementResponse findById(
            @Parameter(description = "Identifiant du paiement") @PathVariable Long id) {
        return paiementMapper.toResponse(paiementService.getPaiementById(id));
    }
}
