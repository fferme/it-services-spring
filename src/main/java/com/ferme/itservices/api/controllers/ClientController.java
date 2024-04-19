package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@Transactional
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/clients", produces = {"application/json"})
@Tag(name = "Client Controller")
public class ClientController {
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "Recupera lista de clientes", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso ao recuperar lista de clientes",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))
        }),
        @ApiResponse(responseCode = "400", description = "Erro ao realizar requisição", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
    })
    public List<Client> listAll() {
        return clientService.listAll();
    }

    @Operation(summary = "Recupera cliente pelo ID", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso ao recuperar cliente pelo id",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
        @ApiResponse(responseCode = "400", description = "ID informado inválido", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado com ID informado", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
    })
    @GetMapping("/{id}")
    public Optional<Client> findById(@PathVariable @NotNull Long id) {
        return clientService.findById(id);
    }

    @Operation(summary = "Recupera cliente pelo nome", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso ao recuperar cliente pelo nome",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
        @ApiResponse(responseCode = "400", description = "Nome informado inválido", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado com nome informado", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
    })
    @GetMapping("/name/{name}")
    public Optional<Client> findByName(@PathVariable @NotNull String name) {
        return clientService.findByName(name);
    }

    @Operation(summary = "Salva novo cliente", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso ao gravar cliente no banco",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
        @ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
    })
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Client create(@RequestBody @Valid @NotNull Client client) {
        return clientService.create(client);
    }

//    @Operation(summary = "Atualiza cliente existente", method = "PUT")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "201", description = "Sucesso ao atualizar cliente no banco",
//            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
//        @ApiResponse(responseCode = "404", description = "Cliente não encontrado com ID informado", content = @Content()),
//        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
//            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
//        })
//    })
//    @PutMapping("/{id}")
//    public Client update(@PathVariable @NotNull UUID id, @RequestBody @Valid @NotNull Client newClient) {
//        return clientService.update(id, newClient);
//    }

    @Operation(summary = "Deleta cliente existente", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente deletado e não encontrado",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado com ID informado", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull Long id) {
        clientService.deleteById(id);
    }

    @Operation(summary = "Deleta todos clientes existentes", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente deletados e não encontrados",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
    })
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAll() {
        clientService.deleteAll();
    }

    @Operation(summary = "Importa clientes de arquivo e salva no banco", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso ao gravar clientes no banco",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
        @ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
        })
    })
    @PostMapping("/import")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void importClients() throws IOException {
        clientService.exportDataToClient();
    }
}