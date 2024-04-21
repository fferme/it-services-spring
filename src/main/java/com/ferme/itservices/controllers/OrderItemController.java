package com.ferme.itservices.controllers;

import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.services.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/orderItems", produces = {"application/json"})
@Tag(name = "OrderItem Controller")
public class OrderItemController {
	private OrderItemService orderItemService;

	@Operation(summary = "Recupera lista de itens de pedido", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Sucesso ao recuperar lista de itens de pedido",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))
				}),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping
	public ResponseEntity<List<OrderItem>> listAll() {
		List<OrderItem> orderItems = orderItemService.listAll();
		return ResponseEntity.ok(orderItems);
	}

	@Operation(summary = "Recupera item de pedido pelo ID", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Sucesso ao recuperar item de pedido pelo id",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "400", description = "ID informado inválido", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Item de pedido não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/{id}")
	public ResponseEntity<OrderItem> findById(@PathVariable("id") Long id) {
		return orderItemService.findById(id)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Cria novo item de pedido", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Sucesso ao criar item de pedido",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PostMapping
	public ResponseEntity<OrderItem> create(@RequestBody @Valid OrderItem orderItem) {
		OrderItem createdOrderItem = orderItemService.create(orderItem);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItem);
	}

	@Operation(summary = "Atualiza item de pedido existente", method = "PUT")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Sucesso ao atualizar item de pedido",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "404", description = "Item de pedido não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PutMapping("/{id}")
	public ResponseEntity<OrderItem> update(@PathVariable("id") Long id, @RequestBody @Valid OrderItem updatedOrderItem) {
		OrderItem modifiedOrderItem = orderItemService.update(id, updatedOrderItem);
		return (modifiedOrderItem != null)
			? ResponseEntity.ok(modifiedOrderItem)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Deleta item de pedido existente", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Item de pedido deletado com sucesso",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "404", description = "Item de pedido não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
		orderItemService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Deleta todos itens de pedido existentes", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Itens de pedido deletados com sucesso",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		orderItemService.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Importa itens de pedido de arquivo e salva no banco", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Sucesso ao gravar itens de pedido no banco",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PostMapping("/import")
	public ResponseEntity<List<OrderItem>> importOrderItems() throws IOException {
		List<OrderItem> orderItems = orderItemService.exportDataToOrderItem();
		return ResponseEntity.status(HttpStatus.CREATED).body(orderItems);
	}
}