package com.ferme.itservices.controllers;

import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.services.OrderItemService;
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
	public ResponseEntity<List<OrderItemDTO>> listAll() {
		List<OrderItemDTO> orderItemsDTO = orderItemService.listAll();
		return ResponseEntity.ok(orderItemsDTO);
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
	public ResponseEntity<OrderItemDTO> findById(@PathVariable("id") UUID id) {
		OrderItemDTO orderItemDTO = orderItemService.findById(id);

		return (orderItemDTO != null)
			? ResponseEntity.ok(orderItemDTO)
			: ResponseEntity.notFound().build();
	}

	@Operation(summary = "Recupera item de ordem de serviço pela descrição", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Sucesso ao recuperar ordem de serviço pela descrição",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "400", description = "Descrição informado inválido", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Item de ordem de serviço não encontrado com descrição informada", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/name/{name}")
	public ResponseEntity<OrderItemDTO> findByDescription(@PathVariable("description") @NotEmpty String description) {
		OrderItemDTO orderItemDTO = orderItemService.findByDescription(description);

		return (orderItemDTO != null)
			? ResponseEntity.ok(orderItemDTO)
			: ResponseEntity.notFound().build();
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
	public ResponseEntity<OrderItemDTO> create(@RequestBody @Valid OrderItemDTO orderItemDTO) {
		OrderItemDTO createdOrderItemDTO = orderItemService.create(orderItemDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItemDTO);
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
	public ResponseEntity<OrderItemDTO> update(@PathVariable("id") UUID id, @RequestBody @Valid OrderItemDTO updatedOrderItemDTO) {
		OrderItemDTO modifiedOrderItemDTO = orderItemService.update(id, updatedOrderItemDTO);

		return (modifiedOrderItemDTO != null)
			? ResponseEntity.ok(modifiedOrderItemDTO)
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
	public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
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
	@Generated
	@PostMapping("/import")
	public ResponseEntity<List<OrderItemDTO>> importOrderItems() throws IOException {
		List<OrderItemDTO> orderItemDTOs = orderItemService.exportDataToOrderItem();
		return ResponseEntity.status(HttpStatus.CREATED).body(orderItemDTOs);
	}
}