package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.dtos.OrderDTO;
import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.services.OrderService;
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

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/orders", produces = {"application/json"})
@Tag(name = "Order Controller")
public class OrderController {
	private OrderService orderService;

	@Operation(summary = "Recupera lista de pedidos", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Sucesso ao recuperar lista de pedidos",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
				}),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping
	public ResponseEntity<List<OrderDTO>> listAll() {
		List<OrderDTO> ordersDTO = orderService.listAll();
		return ResponseEntity.ok(ordersDTO);
	}

	@Operation(summary = "Recupera pedido pelo ID", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Sucesso ao recuperar pedido pelo id",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(responseCode = "400", description = "ID informado inválido", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Pedido não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> findById(@PathVariable("id") UUID id) {
		OrderDTO orderDTO = orderService.findById(id);

		return (orderDTO != null)
			? ResponseEntity.ok(orderDTO)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Cria novo pedido", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Sucesso ao criar pedido",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PostMapping
	public ResponseEntity<OrderDTO> create(@RequestBody @Valid OrderDTO orderDTO) {
		OrderDTO createdOrderDTO = orderService.create(orderDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDTO);
	}

	@Operation(summary = "Atualiza pedido existente", method = "PUT")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Sucesso ao atualizar pedido",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(responseCode = "404", description = "Pedido não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PutMapping("/{id}")
	public ResponseEntity<OrderDTO> update(@PathVariable("id") UUID id, @RequestBody @Valid OrderDTO updatedOrderDTO) {
		OrderDTO modifiedOrderDTO = orderService.update(id, updatedOrderDTO);
		return (modifiedOrderDTO != null)
			? ResponseEntity.ok(modifiedOrderDTO)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Deleta pedido existente", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Pedido deletado com sucesso",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(responseCode = "404", description = "Pedido não encontrado com ID informado", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
		orderService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Deleta todos pedidos existentes", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Pedidos deletados com sucesso",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		orderService.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/import")
	public void importOrders() {
		orderService.importOrders("");
	}
}