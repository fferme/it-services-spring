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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.common.OrderConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
		when(orderService.create(any(Order.class))).thenReturn(VALID_ORDER);

		MvcResult mvcResult = mockMvc.perform(
				post("/api/orders")
					.content(objectMapper.writeValueAsString(VALID_ORDER))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())

			.andExpect(jsonPath("$.header").value(VALID_ORDER.getHeader()))
			.andExpect(jsonPath("$.deviceName").value(VALID_ORDER.getDeviceName()))
			.andExpect(jsonPath("$.deviceSN").value(VALID_ORDER.getDeviceSN()))
			.andExpect(jsonPath("$.problems").value(VALID_ORDER.getProblems()))

			.andExpect(jsonPath("$.client").value(VALID_ORDER.getClient()))
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		Order order = objectMapper.readValue(responseBody, Order.class);

		List<OrderItem> actualOrderItems = order.getOrderItems();
		List<OrderItem> expectedOrderItems = VALID_ORDER.getOrderItems();

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
				post("/api/orders").content(objectMapper.writeValueAsString(VALID_ORDER))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() throws Exception {
		when(orderService.findById(1L)).thenReturn(Optional.ofNullable(VALID_ORDER));

		MvcResult mvcResult = mockMvc.perform(get("/api/orders/1"))
			.andExpect(status().isOk())

			.andExpect(jsonPath("$.header").value(VALID_ORDER.getHeader()))
			.andExpect(jsonPath("$.deviceName").value(VALID_ORDER.getDeviceName()))
			.andExpect(jsonPath("$.deviceSN").value(VALID_ORDER.getDeviceSN()))
			.andExpect(jsonPath("$.problems").value(VALID_ORDER.getProblems()))

			.andExpect(jsonPath("$.client").value(VALID_ORDER.getClient()))
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		Order order = objectMapper.readValue(responseBody, Order.class);

		List<OrderItem> actualOrderItems = order.getOrderItems();
		List<OrderItem> expectedOrderItems = VALID_ORDER.getOrderItems();

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

}