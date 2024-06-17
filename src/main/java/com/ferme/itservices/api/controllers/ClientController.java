package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/clients", produces = {"application/json"})
@Tag(name = "Client Controller")
public class ClientController {
	private ClientService clientService;

	@GetMapping
	@Operation(summary = "Retrieve list of clients", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved list of clients",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))
				}),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	public ResponseEntity<List<ClientDTO>> listAll() {
		List<ClientDTO> clients = clientService.listAll();
		return ResponseEntity.ok(clients);
	}

	@Operation(summary = "Retrieve client by ID", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved client by ID",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid ID provided", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Client not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/{id}")
	public ResponseEntity<ClientDTO> findById(@PathVariable("id") UUID id) {
		ClientDTO clientDTO = clientService.findById(id);

		return (clientDTO != null)
			? ResponseEntity.ok(clientDTO)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Retrieve client by name", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved client by name",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid name provided", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Client not found with the provided name", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/name/{name}")
	public ResponseEntity<ClientDTO> findByName(@PathVariable("name") @NotEmpty String name) {
		ClientDTO clientDTO = clientService.findByName(name);

		return (clientDTO != null)
			? ResponseEntity.ok(clientDTO)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Retrieve client by name and phone number", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved client by name and phone number",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid name and phone number provided", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Client not found with the provided name and phone number", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/name/{name}/phoneNumber/{phoneNumber}")
	public ResponseEntity<ClientDTO> findByNameAndPhoneNumber(@PathVariable("name") @NotEmpty String name, @PathVariable("phoneNumber") @NotEmpty String phoneNumber) {
		ClientDTO clientDTO = clientService.findByNameAndPhoneNumber(name, phoneNumber);

		return (clientDTO != null)
			? ResponseEntity.ok(clientDTO)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Save a new client", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully saved client to the database",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Request not found", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PostMapping
	public ResponseEntity<ClientDTO> create(@RequestBody @Valid ClientDTO clientDTO) {
		ClientDTO createdClientDTO = clientService.create(clientDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdClientDTO);
	}

	@Operation(summary = "Update an existing client", method = "PUT")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully updated client in the database",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Client not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PutMapping("/{id}")
	public ResponseEntity<ClientDTO> update(@PathVariable("id") UUID id, @RequestBody @Valid ClientDTO updatedClientDTO) {
		ClientDTO modifiedClientDTO = clientService.update(id, updatedClientDTO);

		return (modifiedClientDTO != null)
			? ResponseEntity.ok(modifiedClientDTO)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Delete an existing client", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Client deleted and not found",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Client not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
		clientService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Delete all existing clients", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Clients deleted and not found",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		clientService.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Import clients from file and save to the database", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully saved clients to the database",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Request not found", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@Generated
	@GetMapping("/import")
	public ResponseEntity<List<ClientDTO>> importClients() {
		List<ClientDTO> clientsDTO = clientService.exportDataToClient();
		return ResponseEntity.status(HttpStatus.CREATED).body(clientsDTO);
	}
}
