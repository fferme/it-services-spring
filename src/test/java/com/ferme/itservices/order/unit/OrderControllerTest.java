package com.ferme.itservices.order.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.controllers.OrderController;
import com.ferme.itservices.dtos.OrderDTO;
import com.ferme.itservices.order.utils.OrderAssertions;
import com.ferme.itservices.order.utils.OrderConstants;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.order.utils.OrderConstants.ORDER_A_UUID;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

	private final OrderConstants orderConstants = OrderConstants.getInstance();
	private final OrderAssertions orderAssertions = OrderAssertions.getInstance();

	@Test
	public void createOrder_WithValidData_ReturnsCreated() throws Exception {
		final OrderDTO newOrderDTO = orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS_DTO;

		when(orderService.create(newOrderDTO)).thenReturn(newOrderDTO);

		MvcResult mvcResult = mockMvc.perform(
				post("/api/orders")
					.content(objectMapper.writeValueAsString(newOrderDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		OrderDTO orderDTO = objectMapper.readValue(responseBody, OrderDTO.class);

		orderAssertions.assertOrderProps(newOrderDTO, orderDTO);
	}

	@Test
	public void createOrder_WithInvalidData_ReturnsUnprocessableEntity() throws Exception {
		final OrderDTO invalidOrderDTO = orderConstants.INVALID_ORDER_DTO;

		mockMvc
			.perform(
				post("/api/orders").content(objectMapper.writeValueAsString(invalidOrderDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void createOrder_WithExistingDeviceSN_ReturnsConflict() throws Exception {
		final OrderDTO newOrderDTO = orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS_DTO;

		when(orderService.create(newOrderDTO)).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/orders").content(objectMapper.writeValueAsString(newOrderDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void updateOrder_WithValidDataAndId_ReturnsOk() throws Exception {
		final OrderDTO newOrderDTO = orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS_DTO;

		when(orderService.update(ORDER_A_UUID, newOrderDTO)).thenReturn(newOrderDTO);

		MvcResult mvcResult = mockMvc
			.perform(
				put("/api/orders/" + ORDER_A_UUID)
					.content(objectMapper.writeValueAsString(newOrderDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		OrderDTO orderDTO = objectMapper.readValue(responseBody, OrderDTO.class);

		orderAssertions.assertOrderProps(newOrderDTO, orderDTO);
	}

	@Test
	public void updateOrder_WithUnexistentId_ReturnsNotFound() throws Exception {
		final OrderDTO newOrderDTO = orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS_DTO;

		when(orderService.update(eq(UUID.randomUUID()), any(OrderDTO.class))).thenReturn(null);

		mockMvc
			.perform(
				put("/api/orders/" + UUID.randomUUID())
					.content(objectMapper.writeValueAsString(newOrderDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() throws Exception {
		final OrderDTO orderDTO = orderConstants.ORDER_DTO;

		when(orderService.findById(ORDER_A_UUID)).thenReturn(orderDTO);

		MvcResult mvcResult = mockMvc.perform(get("/api/orders/" + ORDER_A_UUID))
			.andExpect(status().isOk())
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		OrderDTO readOrderDTO = objectMapper.readValue(responseBody, OrderDTO.class);

		orderAssertions.assertOrderProps(orderDTO, readOrderDTO);
	}

	@Test
	public void getOrder_ByUnexistingId_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/orders/" + ORDER_A_UUID))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listOrders_WhenOrdersExists_ReturnsAllOrders() throws Exception {
		final List<OrderDTO> ordersDTO = orderConstants.ORDERS_DTO;

		when(orderService.listAll()).thenReturn(ordersDTO);

		mockMvc
			.perform(get("/api/orders"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void listOrders_WhenOrdersDoesNotExists_ReturnsEmptyList() throws Exception {
		when(orderService.listAll()).thenReturn(Collections.emptyList());

		mockMvc
			.perform(get("/api/orders"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void removeOrder_WithExistingId_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/orders/" + ORDER_A_UUID))
			.andExpect(status().isNoContent());
	}

	@Test
	public void removeAllOrders_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/orders"))
			.andExpect(status().isNoContent());
	}
}