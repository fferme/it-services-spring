package com.ferme.itservices.orderItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.controllers.OrderItemController;
import com.ferme.itservices.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static com.ferme.itservices.orderItem.OrderItemConstants.*;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
		when(orderItemService.create(ORDERITEM_A)).thenReturn(ORDERITEM_A);

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(ORDERITEM_A))
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
				post("/api/orderItems").content(objectMapper.writeValueAsString(ORDERITEM_A))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void updateOrderItem_WithValidDataAndId_ReturnsOk() throws Exception {
		when(orderItemService.update(eq(NEW_ORDERITEM_A.getId()), any())).thenReturn(NEW_ORDERITEM_A);

		mockMvc.perform(put("/api/orderItems/1")
			                .content(objectMapper.writeValueAsString(NEW_ORDERITEM_A))
			                .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.orderItemType").value(NEW_ORDERITEM_A.getOrderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(NEW_ORDERITEM_A.getDescription()))
			.andExpect(jsonPath("$.price").value(NEW_ORDERITEM_A.getPrice()));
	}

	@Test
	public void updateOrderItem_WithUnexistentId_ReturnsNotFound() throws Exception {
		when(orderItemService.update(eq(5L), any())).thenReturn(null);

		mockMvc.perform(put("/api/orderItems/5")
			                .content(objectMapper.writeValueAsString(NEW_ORDERITEM_A))
			                .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() throws Exception {
		when(orderItemService.findById(1L)).thenReturn(Optional.of(ORDERITEM_A));

		mockMvc.perform(get("/api/orderItems/1"))
			.andExpect(status().isOk())

			.andExpect(jsonPath("$.orderItemType").value(ORDERITEM_A.getOrderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(ORDERITEM_A.getDescription()))
			.andExpect(jsonPath("$.price").value(ORDERITEM_A.getPrice()));
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/orderItems/1"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listOrderItems_WhenOrderItemExists_ReturnsAllOrderItemsSortedByDescription() throws Exception {
		when(orderItemService.listAll()).thenReturn(ORDER_ITEMS);

		mockMvc.perform(get("/api/orderItems"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[*].description", containsInRelativeOrder(
				ORDERITEM_A.getDescription(),
				ORDERITEM_B.getDescription(),
				ORDERITEM_C.getDescription()
			)));
	}

	@Test
	public void listOrderItems_ReturnsNoOrderItems() throws Exception {
		when(orderItemService.listAll()).thenReturn(Collections.emptyList());

		mockMvc
			.perform(get("/api/orderItems"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void removeOrderItem_WithExistingId_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/orderItems/1"))
			.andExpect(status().isNoContent());
	}

	@Test
	public void removeOrderItem_WithUnexistingId_ReturnsNotFound() throws Exception {
		doThrow(new EmptyResultDataAccessException(1)).when(orderItemService).deleteById(1L);

		mockMvc
			.perform(delete("/api/orderItems/1"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void removeAllOrderItems_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/orderItems"))
			.andExpect(status().isNoContent());
	}
}