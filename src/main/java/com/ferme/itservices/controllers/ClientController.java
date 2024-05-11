package com.ferme.itservices.controllers;

import com.ferme.itservices.models.Client;
import com.ferme.itservices.services.ClientService;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/clients", produces = {"application/json"})
@Tag(name = "Client Controller")
public class ClientController {
	private ClientService clientService;

	@GetMapping
	@Operation(summary = "Recupera lista de clientes", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Sucesso ao recuperar lista de clientes",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))
				}),
			@ApiResponse(responseCode = "400", description = "Erro ao realizar requisição", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	public ResponseEntity<List<Client>> listAll() {
		List<Client> clients = clientService.listAll();
		return ResponseEntity.ok(clients);
	}

	@Operation(summary = "Recupera cliente pelo ID", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Sucesso ao recuperar cliente pelo id",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "400", description = "ID informado inválido", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/{id}")
	public ResponseEntity<Client> findById(@PathVariable("id") UUID id) {
		return clientService.findById(id).map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Recupera cliente pelo nome", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Sucesso ao recuperar cliente pelo nome",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "400", description = "Nome informado inválido", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado com nome informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/name/{name}")
	public ResponseEntity<Client> findByName(@PathVariable("name") @NotEmpty String name) {
		return clientService.findByName(name).map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Salva novo cliente", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Sucesso ao gravar cliente no banco",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PostMapping
	public ResponseEntity<Client> create(@RequestBody @Valid Client client) {
		Client createdClient = clientService.create(client);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
	}

	@Operation(summary = "Atualiza cliente existente", method = "PUT")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Sucesso ao atualizar cliente no banco",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PutMapping("/{id}")
	public ResponseEntity<Client> update(@PathVariable("id") UUID id, @RequestBody @Valid Client updatedClient) {
		Client modifiedClient = clientService.update(id, updatedClient);
		return (modifiedClient != null)
			? ResponseEntity.ok(modifiedClient)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Deleta cliente existente", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Cliente deletado e não encontrado",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remove(@PathVariable("id") UUID id) {
		clientService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Deleta todos clientes existentes", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Cliente deletados e não encontrados",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping
	public ResponseEntity<Void> remove() {
		clientService.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Importa clientes de arquivo e salva no banco", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Sucesso ao gravar clientes no banco",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@Generated
	@PostMapping("/import")
	public ResponseEntity<List<Client>> importClients() throws IOException {
		List<Client> clients = clientService.exportDataToClient();
		return ResponseEntity.status(HttpStatus.CREATED).body(clients);
	}
}