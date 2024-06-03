package com.ferme.itservices.orderItem.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.controllers.OrderItemController;
import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.orderItem.utils.OrderItemConstants;
import com.ferme.itservices.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.ferme.itservices.dtos.mappers.OrderItemMapper.toOrderItemDTO;
import static com.ferme.itservices.dtos.mappers.OrderItemMapper.toOrderItemDTOList;
import static com.ferme.itservices.orderItem.utils.OrderItemConstants.ORDERITEM_A_UUID;
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

	private static final OrderItemConstants orderItemConstants = OrderItemConstants.getInstance();

	@Test
	public void createOrder_WithValidData_ReturnsCreated() throws Exception {
		final OrderItem orderItem = orderItemConstants.ORDERITEM;
		final OrderItemDTO orderItemDTO = toOrderItemDTO(orderItem);

		when(orderItemService.create(orderItemDTO)).thenReturn(orderItemDTO);

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(orderItemDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())

			.andExpect(jsonPath("$.orderItemType").value(orderItem.getOrderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(orderItem.getDescription()))
			.andExpect(jsonPath("$.price").value(orderItem.getPrice()));
	}

	@Test
	public void createOrderItem_WithInvalidData_ReturnsBadRequest() throws Exception {
		final OrderItem emptyOrderItem = orderItemConstants.EMPTY_ORDERITEM;
		final OrderItem invalidOrderItem = orderItemConstants.INVALID_ORDERITEM;

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(emptyOrderItem))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(invalidOrderItem))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void createOrderItem_WithExistingDescription_ReturnsConflict() throws Exception {
		final OrderItem orderItem = orderItemConstants.ORDERITEM;

		when(orderItemService.create(any(OrderItemDTO.class))).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/orderItems").content(objectMapper.writeValueAsString(orderItem))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void updateOrderItem_WithValidDataAndId_ReturnsOk() throws Exception {
		final OrderItemDTO newOrderItemDTO = toOrderItemDTO(orderItemConstants.NEW_ORDERITEM);

		when(orderItemService.update(ORDERITEM_A_UUID, newOrderItemDTO)).thenReturn(newOrderItemDTO);

		mockMvc.perform(put("/api/orderItems/" + ORDERITEM_A_UUID)
			                .content(objectMapper.writeValueAsString(newOrderItemDTO))
			                .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.orderItemType").value(newOrderItemDTO.orderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(newOrderItemDTO.description()))
			.andExpect(jsonPath("$.price").value(newOrderItemDTO.price()));
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() throws Exception {
		final OrderItemDTO orderItemDTO = toOrderItemDTO(orderItemConstants.ORDERITEM);

		when(orderItemService.findById(ORDERITEM_A_UUID)).thenReturn(orderItemDTO);

		mockMvc.perform(get("/api/orderItems/" + ORDERITEM_A_UUID))
			.andExpect(status().isOk())

			.andExpect(jsonPath("$.orderItemType").value(orderItemDTO.orderItemType().getValue()))
			.andExpect(jsonPath("$.description").value(orderItemDTO.description()))
			.andExpect(jsonPath("$.price").value(orderItemDTO.price()));
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsNotFound() throws Exception {
		when(orderItemService.findById(ORDERITEM_A_UUID)).thenReturn(null);

		mockMvc.perform(get("/api/orderItems/" + ORDERITEM_A_UUID))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listOrderItems_WhenOrderItemExists_ReturnsAllOrderItemsSortedByDescription() throws Exception {
		final List<OrderItem> orderItems = orderItemConstants.ORDER_ITEMS;
		final List<OrderItemDTO> orderItemsDTO = toOrderItemDTOList(orderItems);

		when(orderItemService.listAll()).thenReturn(orderItemsDTO);

		mockMvc.perform(get("/api/orderItems"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[*].description", containsInRelativeOrder(
				orderItemsDTO.get(0).description(),
				orderItemsDTO.get(1).description(),
				orderItemsDTO.get(2).description()
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