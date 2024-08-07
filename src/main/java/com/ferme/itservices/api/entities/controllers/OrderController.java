package com.ferme.itservices.api.entities.controllers;

import com.ferme.itservices.api.application.controllers.GenericCRUDController;
import com.ferme.itservices.api.entities.dtos.OrderDTO;
import com.ferme.itservices.api.entities.models.Client;
import com.ferme.itservices.api.entities.models.Order;
import com.ferme.itservices.api.entities.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/api/orders", produces = {"application/json"})
@Tag(name = "Order Controller")
public class OrderController implements GenericCRUDController<OrderDTO, UUID> {
	private OrderService orderService;

	@Override
	@Operation(summary = "Retrieve list of orders", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved list of orders",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
				}),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping
	public ResponseEntity<List<OrderDTO>> listAll() {
		List<OrderDTO> ordersDTO = orderService.listAll();
		return ResponseEntity.ok(ordersDTO);
	}

	@Override
	@Operation(summary = "Retrieve order by ID", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved order by ID",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid ID provided", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Order not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
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

	@Override
	@Operation(summary = "Create a new order", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully created order",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(responseCode = "404", description = "Request not found", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PostMapping
	public ResponseEntity<OrderDTO> create(@RequestBody @Valid OrderDTO orderDTO) {
		OrderDTO createdOrderDTO = orderService.create(orderDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDTO);
	}

	@Override
	@Operation(summary = "Update an existing order", method = "PUT")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully updated order",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(responseCode = "404", description = "Order not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
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

	@Override
	@Operation(summary = "Delete an existing order", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Order successfully deleted",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(responseCode = "404", description = "Order not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
		orderService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@Operation(summary = "Delete all existing orders", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Orders successfully deleted",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))}),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		orderService.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Import orders from file and save to the database", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully saved orders to the database",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Request not found", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@Generated
	@GetMapping("/import")
	public ResponseEntity<List<OrderDTO>> importOrders() {
		List<OrderDTO> ordersDTO = orderService.importOrders();
		return ResponseEntity.status(HttpStatus.CREATED).body(ordersDTO);
	}
}
