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
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientRequest;
import tn.esprit.autoloc.web.dto.ClientResponse;
import tn.esprit.autoloc.web.mapper.ClientMapper;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Clients", description = "Gestion des clients")
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService clientService;
    private final ClientMapper clientMapper;

    @Operation(summary = "Lister les clients")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée (éventuellement vide)")
    @GetMapping
    public List<ClientResponse> findAll() {
        return clientService.getAllClients().stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Consulter un client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @GetMapping("/{id}")
    public ClientResponse findById(
            @Parameter(description = "Identifiant du client") @PathVariable Long id) {
        return clientMapper.toResponse(clientService.getClientById(id));
    }

    @Operation(summary = "Créer un client",
               description = "L'identifiant est généré automatiquement. La date d'inscription est fixée à aujourd'hui.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides (email ou permis déjà utilisé, champ obligatoire manquant)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse create(@Valid @RequestBody ClientRequest dto) {
        Client client = clientMapper.toEntity(dto);
        return clientMapper.toResponse(clientService.createClient(client));
    }

    @Operation(summary = "Modifier un client",
               description = "Seuls les champs non nuls du corps sont recopiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @PutMapping("/{id}")
    public ClientResponse update(
            @Parameter(description = "Identifiant du client") @PathVariable Long id,
            @Valid @RequestBody ClientRequest dto) {
        Client client = clientMapper.toEntity(dto);
        return clientMapper.toResponse(clientService.updateClient(id, client));
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client supprimé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant du client") @PathVariable Long id) {
        clientService.deleteClient(id);
    }
}
