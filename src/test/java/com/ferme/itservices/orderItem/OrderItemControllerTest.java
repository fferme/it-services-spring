package com.ferme.itservices.orderItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.controllers.OrderItemController;
import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.ferme.itservices.orderItem.OrderItemConstants.*;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
		when(orderItemService.create(ORDERITEM_A_DTO)).thenReturn(ORDERITEM_A_DTO);

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(ORDERITEM_A_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())

			.andExpect(jsonPath("$.orderItemType").value(ORDERITEM_A.getOrderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(ORDERITEM_A.getDescription()))
			.andExpect(jsonPath("$.price").value(ORDERITEM_A.getPrice()));
	}

	@Test
	public void createOrderItem_WithInvalidData_ReturnsBadRequest() throws Exception {
		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(EMPTY_ORDERITEM_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(INVALID_ORDERITEM_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void createOrderItem_WithExistingDescription_ReturnsConflict() throws Exception {
		when(orderItemService.create(any(OrderItemDTO.class))).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(ORDERITEM_A))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void updateOrderItem_WithValidDataAndId_ReturnsOk() throws Exception {
		when(orderItemService.update(ORDERITEM_A_UUID, NEW_ORDERITEM_DTO)).thenReturn(NEW_ORDERITEM_DTO);

		mockMvc.perform(put("/api/orderItems/" + ORDERITEM_A_UUID)
			                .content(objectMapper.writeValueAsString(NEW_ORDERITEM_DTO))
			                .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.orderItemType").value(NEW_ORDERITEM_DTO.orderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(NEW_ORDERITEM_DTO.description()))
			.andExpect(jsonPath("$.price").value(NEW_ORDERITEM_DTO.price()));
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() throws Exception {
		when(orderItemService.findById(ORDERITEM_A_UUID)).thenReturn(ORDERITEM_A_DTO);

		mockMvc.perform(get("/api/orderItems/" + ORDERITEM_A_UUID))
			.andExpect(status().isOk())

			.andExpect(jsonPath("$.orderItemType").value(ORDERITEM_A_DTO.orderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(ORDERITEM_A_DTO.description()))
			.andExpect(jsonPath("$.price").value(ORDERITEM_A_DTO.price()));
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsNotFound() throws Exception {
		when(orderItemService.findById(ORDERITEM_A_UUID)).thenReturn(null);

		mockMvc.perform(get("/api/orderItems/" + ORDERITEM_A_UUID))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listOrderItems_WhenOrderItemExists_ReturnsAllOrderItemsSortedByDescription() throws Exception {
		when(orderItemService.listAll()).thenReturn(ORDER_ITEMS_DTO);

		mockMvc.perform(get("/api/orderItems"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[*].description", containsInRelativeOrder(
				ORDERITEM_A_DTO.description(),
				ORDERITEM_B_DTO.description(),
				ORDERITEM_C_DTO.description()
			)));
	}

	@Test
	public void listOrderItems_WhenOrderItemsDoesNotExists_ReturnsNoOrderItems() throws Exception {
		when(orderItemService.listAll()).thenReturn(Collections.emptyList());

		mockMvc
			.perform(get("/api/orderItems"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void removeOrderItem_WithExistingId_ReturnsNoContent() throws Exception {
		doNothing().when(orderItemService).deleteById(ORDERITEM_A_UUID);

		mockMvc
			.perform(delete("/api/orderItems/" + ORDERITEM_A_UUID))
			.andExpect(status().isNoContent());
	}

	@Test
	public void removeAllOrderItems_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/orderItems"))
			.andExpect(status().isNoContent());
	}
}