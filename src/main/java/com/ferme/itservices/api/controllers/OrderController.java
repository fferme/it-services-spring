package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.services.OrderService;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RestController
@Transactional
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/orders", produces = {"application/json"})
@Tag(name = "Order-Controller")
public class OrderController {
    private OrderService orderService;

    @Operation(summary = "Recupera lista de pedidos", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao recuperar lista de pedidos",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @GetMapping
    public List<Order> listAll() {
        return orderService.listAll();
    }

    @Operation(summary = "Recupera pedido pelo ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao recuperar pedido pelo id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "ID informado inválido", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado com ID informado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @GetMapping("/{id}")
    public Optional<Order> findById(@PathVariable @NotNull UUID id) {
        return orderService.findById(id);
    }

    @Operation(summary = "Cria novo pedido", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso ao criar pedido",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Order create(@RequestBody @Valid @NotNull Order order) {
        return orderService.create(order);
    }

    @Operation(summary = "Atualiza pedido existente", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso ao atualizar pedido",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado com ID informado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @PutMapping("/{id}")
    public Order update(@PathVariable @NotNull UUID id, @RequestBody @Valid @NotNull Order newOrder) {
        return orderService.update(id, newOrder);
    }

    @Operation(summary = "Deleta pedido existente", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado com ID informado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull UUID id) {
        orderService.deleteById(id);
    }

    @Operation(summary = "Deleta todos pedidos existentes", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedidos deletados com sucesso",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAll() {
        orderService.deleteAll();
    }
}