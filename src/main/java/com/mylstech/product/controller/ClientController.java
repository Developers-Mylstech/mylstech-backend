package com.mylstech.product.controller;

import com.mylstech.product.dto.request.ClientRequest;
import com.mylstech.product.dto.response.ClientResponse;
import com.mylstech.product.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Client management APIs")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    @Operation(
            summary = "Add a new client", 
            description = "Creates a new client and returns the client details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ClientResponse> addClient(
            @Parameter(description = "Client details to create", required = true)
            @Valid @RequestBody ClientRequest request) {
        return new ResponseEntity<>(clientService.addClient(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Get all clients", 
            description = "Returns a list of all clients"
    )
    @ApiResponse(responseCode = "200", description = "List of clients retrieved successfully")
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{clientId}")
    @Operation(
            summary = "Get client by ID", 
            description = "Returns a client by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ClientResponse> getClientById(
            @Parameter(description = "ID of the client to retrieve", required = true)
            @PathVariable Long clientId) {
        return clientService.getClientById(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{clientId}")
    @Operation(
            summary = "Update a client", 
            description = "Updates a client and returns the updated client details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ClientResponse> updateClient(
            @Parameter(description = "ID of the client to update", required = true)
            @PathVariable Long clientId,
            @Parameter(description = "Updated client details", required = true)
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.updateClient(clientId, request));
    }

    @DeleteMapping("/{clientId}")
    @Operation(summary = "Delete a client", description = "Deletes a client by its ID")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clientId}/image")
    @Operation(summary = "Delete client image", description = "Removes the image from a client")
    public ResponseEntity<ClientResponse> deleteClientImage(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.deleteClientImage(clientId));
    }
}