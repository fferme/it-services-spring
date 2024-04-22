package com.ferme.itservices.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.controllers.OrderController;
import com.ferme.itservices.models.Order;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.common.OrderConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OrderService orderService;

	@Test
	public void createOrder_ValidData_ReturnsCreated() throws Exception {
		when(orderService.create(any(Order.class))).thenReturn(ORDER_A);

		MvcResult mvcResult = mockMvc.perform(
				post("/api/orders")
					.content(objectMapper.writeValueAsString(ORDER_A))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())

			.andExpect(jsonPath("$.header").value(ORDER_A.getHeader()))
			.andExpect(jsonPath("$.deviceName").value(ORDER_A.getDeviceName()))
			.andExpect(jsonPath("$.deviceSN").value(ORDER_A.getDeviceSN()))
			.andExpect(jsonPath("$.problems").value(ORDER_A.getProblems()))

			.andExpect(jsonPath("$.client").value(ORDER_A.getClient()))
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		Order order = objectMapper.readValue(responseBody, Order.class);

		List<OrderItem> actualOrderItems = order.getOrderItems();
		List<OrderItem> expectedOrderItems = ORDER_A.getOrderItems();

		for (int i = 0; i < expectedOrderItems.size(); i++) {
			OrderItem expectedOrderItem = expectedOrderItems.get(i);
			OrderItem actualOrderItem = actualOrderItems.get(i);

			assertEquals(expectedOrderItem.getOrderItemType(), actualOrderItem.getOrderItemType());
			assertEquals(expectedOrderItem.getDescription(), actualOrderItem.getDescription());
			assertEquals(expectedOrderItem.getPrice(), actualOrderItem.getPrice());
		}
	}

	@Test
	public void createOrder_WithInvalidData_ReturnsBadRequest() throws Exception {
		mockMvc
			.perform(
				post("/api/orders").content(objectMapper.writeValueAsString(EMPTY_ORDER))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());

		mockMvc
			.perform(
				post("/api/orders").content(objectMapper.writeValueAsString(INVALID_ORDER))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void createOrder_WithExistingPhoneNumber_ReturnsConflict() throws Exception {
		when(orderService.create(any())).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/orders").content(objectMapper.writeValueAsString(ORDER_A))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() throws Exception {
		when(orderService.findById(1L)).thenReturn(Optional.of(ORDER_A));

		MvcResult mvcResult = mockMvc.perform(get("/api/orders/1"))
			.andExpect(status().isOk())

			.andExpect(jsonPath("$.header").value(ORDER_A.getHeader()))
			.andExpect(jsonPath("$.deviceName").value(ORDER_A.getDeviceName()))
			.andExpect(jsonPath("$.deviceSN").value(ORDER_A.getDeviceSN()))
			.andExpect(jsonPath("$.problems").value(ORDER_A.getProblems()))

			.andExpect(jsonPath("$.client").value(ORDER_A.getClient()))
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		Order order = objectMapper.readValue(responseBody, Order.class);

		List<OrderItem> actualOrderItems = order.getOrderItems();
		List<OrderItem> expectedOrderItems = ORDER_A.getOrderItems();

		for (int i = 0; i < expectedOrderItems.size(); i++) {
			OrderItem expectedOrderItem = expectedOrderItems.get(i);
			OrderItem actualOrderItem = actualOrderItems.get(i);

			assertEquals(expectedOrderItem.getOrderItemType(), actualOrderItem.getOrderItemType());
			assertEquals(expectedOrderItem.getDescription(), actualOrderItem.getDescription());
			assertEquals(expectedOrderItem.getPrice(), actualOrderItem.getPrice());
		}
	}

	@Test
	public void getOrder_ByUnexistingId_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/orders/1"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listOrders_ReturnsOrders() throws Exception {
		when(orderService.listAll()).thenReturn(ORDERS);

		mockMvc
			.perform(get("/api/orders"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void listOrders_ReturnsNoOrders() throws Exception {
		when(orderService.listAll()).thenReturn(Collections.emptyList());

		mockMvc
			.perform(get("/api/orders"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void removeOrder_WithExistingId_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/orders/1"))
			.andExpect(status().isNoContent());
	}

	@Test
	public void removeOrder_WithUnexistingId_ReturnsNotFound() throws Exception {
		doThrow(new EmptyResultDataAccessException(1)).when(orderService).deleteById(1L);

		mockMvc
			.perform(delete("/api/orders/1"))
			.andExpect(status().isNotFound());
	}
}