package com.empresa.mspersonclient.infrastructure.controller;

import com.empresa.mspersonclient.application.ClientApplicationService;
import com.empresa.mspersonclient.domain.Client;
import com.empresa.mspersonclient.domain.ports.ClientControllerPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Client Management", description = "Operations related to client management")

@RestController
@RequestMapping("/clientes")
public class ClientController implements ClientControllerPort {

    private final ClientApplicationService clientService;

    public ClientController(ClientApplicationService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all clients", description = "Fetches a list of all registered clients.")
    public List<Client> getAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a client by ID", description = "Fetches a specific client based on the provided ID.")
    public Client getClient(
            @Parameter(description = "ID of the client to retrieve", required = true) @PathVariable String id) {
        return clientService.getClientById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client with the provided details.")
    public Client createClient(
            @Parameter(description = "Details of the client to be created", required = true) @Valid @RequestBody Client client) {
        return clientService.createClient(client);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing client", description = "Updates the details of an existing client based on the provided ID.")
    public Client updateClient(
            @Parameter(description = "ID of the client to update", required = true) @PathVariable String id,
            @Parameter(description = "Updated details of the client", required = true) @Valid @RequestBody Client clientDetails) {
        return clientService.updateClient(id, clientDetails);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client by ID", description = "Deletes a specific client based on the provided ID.")
    public String deleteClient(
            @Parameter(description = "ID of the client to delete", required = true) @PathVariable String id) {
        return clientService.deleteClient(id);
    }
}
