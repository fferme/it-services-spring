package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.models.OrderItem;
import com.ferme.itservices.api.services.OrderItemService;
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
import java.util.UUID;

@AllArgsConstructor
@RestController
@Transactional
@CrossOrigin(origins = "*")
@RequestMapping("/api/orderItems")
@Tag(name = "OrderItem-Controller")
public class OrderItemController {
    private OrderItemService orderItemService;

    @Operation(summary = "Recupera lista de itens de pedido", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao recuperar lista de itens de pedido",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @GetMapping
    public List<OrderItem> listAll() {
        return orderItemService.listAll();
    }

    @Operation(summary = "Recupera item de pedido pelo ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao recuperar item de pedido pelo id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
            @ApiResponse(responseCode = "400", description = "ID informado inválido", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Item de pedido não encontrado com ID informado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @GetMapping("/{id}")
    public Optional<OrderItem> findById(@PathVariable @NotNull UUID id) {
        return orderItemService.findById(id);
    }

    @Operation(summary = "Cria novo item de pedido", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso ao criar item de pedido",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
            @ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderItem create(@RequestBody @Valid @NotNull OrderItem orderItem) {
        return orderItemService.create(orderItem);
    }

    @Operation(summary = "Atualiza item de pedido existente", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso ao atualizar item de pedido",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
            @ApiResponse(responseCode = "404", description = "Item de pedido não encontrado com ID informado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @PutMapping("/{id}")
    public OrderItem update(@PathVariable @NotNull UUID id, @RequestBody @Valid @NotNull OrderItem newOrderItem) {
        return orderItemService.update(id, newOrderItem);
    }

    @Operation(summary = "Deleta item de pedido existente", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item de pedido deletado com sucesso",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
            @ApiResponse(responseCode = "404", description = "Item de pedido não encontrado com ID informado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull UUID id) {
        orderItemService.deleteById(id);
    }

    @Operation(summary = "Deleta todos itens de pedido existentes", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Itens de pedido deletados com sucesso",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAll() {
        orderItemService.deleteAll();
    }

    @Operation(summary = "Importa itens de pedido de arquivo e salva no banco", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso ao gravar itens de pedido no banco",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
            @ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @PostMapping("/import")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void importClients() throws IOException {
        orderItemService.exportDataToOrderItem();
    }
}