package com.ferme.itservices.api.orderItem.controllers;

import com.ferme.itservices.api.application.controllers.GenericCRUDController;
import com.ferme.itservices.api.orderItem.dtos.OrderItemDTO;
import com.ferme.itservices.api.orderItem.models.OrderItem;
import com.ferme.itservices.api.orderItem.services.OrderItemService;
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
@RequestMapping(value = "/api/orderItems", produces = {"application/json"})
@Tag(name = "OrderItem Controller")
public class OrderItemController implements GenericCRUDController<OrderItemDTO, UUID> {
	private OrderItemService orderItemService;

	@Override
	@Operation(summary = "Retrieve list of order items", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved list of order items",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))
				}),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping
	public ResponseEntity<List<OrderItemDTO>> listAll() {
		List<OrderItemDTO> orderItemsDTO = orderItemService.listAll();
		return ResponseEntity.ok(orderItemsDTO);
	}

	@Override
	@Operation(summary = "Retrieve order item by ID", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved order item by ID",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid ID provided", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Order item not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
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

	@Operation(summary = "Retrieve order item by description", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200", description = "Successfully retrieved order item by description",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "400", description = "Invalid description provided", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Order item not found with the provided description", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@GetMapping("/description/{description}")
	public ResponseEntity<OrderItemDTO> findByDescription(@PathVariable("description") @NotEmpty String description) {
		OrderItemDTO orderItemDTO = orderItemService.findByDescription(description);

		return (orderItemDTO != null)
			? ResponseEntity.ok(orderItemDTO)
			: ResponseEntity.notFound().build();
	}

	@Override
	@Operation(summary = "Create new order item", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully created order item",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "404", description = "Request not found", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@PostMapping
	public ResponseEntity<OrderItemDTO> create(@RequestBody @Valid OrderItemDTO orderItemDTO) {
		OrderItemDTO createdOrderItemDTO = orderItemService.create(orderItemDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderItemDTO);
	}

	@Override
	@Operation(summary = "Update an existing order item", method = "PUT")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully updated order item",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "404", description = "Order item not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
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

	@Override
	@Operation(summary = "Delete an existing order item", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Order item successfully deleted",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "404", description = "Order item not found with the provided ID", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
		orderItemService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@Operation(summary = "Delete all existing order items", method = "DELETE")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "204", description = "Order items successfully deleted",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		orderItemService.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Import order items from file and save to the database", method = "GET")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully saved order items to the database",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderItem.class))}),
			@ApiResponse(responseCode = "404", description = "Request not found", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
	@Generated
	@GetMapping("/import")
	public ResponseEntity<List<OrderItemDTO>> importOrderItems() {
		List<OrderItemDTO> orderItemDTOs = orderItemService.exportDataToOrderItem();
		return ResponseEntity.status(HttpStatus.CREATED).body(orderItemDTOs);
	}
}
