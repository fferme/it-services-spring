package com.ferme.itservices.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.controllers.OrderItemController;
import com.ferme.itservices.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.ferme.itservices.common.OrderItemConstants.EMPTY_ORDERITEM;
import static com.ferme.itservices.common.OrderItemConstants.VALID_ORDERITEM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderItemController.class)
public class OrderItemControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OrderItemService orderItemService;

	@Test
	public void createOrder_WithValidData_ReturnsCreated() throws Exception {
		when(orderItemService.create(VALID_ORDERITEM)).thenReturn(VALID_ORDERITEM);

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(VALID_ORDERITEM))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())

			.andExpect(jsonPath("$.orderItemType").value(VALID_ORDERITEM.getOrderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(VALID_ORDERITEM.getDescription()))
			.andExpect(jsonPath("$.price").value(VALID_ORDERITEM.getPrice()));
	}

	@Test
	public void createOrderItem_WithInvalidData_ReturnsBadRequest() throws Exception {
		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(EMPTY_ORDERITEM))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(EMPTY_ORDERITEM))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void createOrderItem_WithExistingPhoneNumber_ReturnsConflict() throws Exception {
		when(orderItemService.create(any())).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(VALID_ORDERITEM))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() throws Exception {
		when(orderItemService.findById(1L)).thenReturn(Optional.ofNullable(VALID_ORDERITEM));

		mockMvc.perform(get("/api/orderItems/1"))
			.andExpect(status().isOk())

			.andExpect(jsonPath("$.orderItemType").value(VALID_ORDERITEM.getOrderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(VALID_ORDERITEM.getDescription()))
			.andExpect(jsonPath("$.price").value(VALID_ORDERITEM.getPrice()));
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/orderItems/1"))
			.andExpect(status().isNotFound());
	}

}