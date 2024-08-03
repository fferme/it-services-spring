package com.ferme.itservices.order.unit;

import com.ferme.itservices.api.application.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.entities.dtos.OrderDTO;
import com.ferme.itservices.api.entities.models.Order;
import com.ferme.itservices.api.entities.repositories.ClientRepository;
import com.ferme.itservices.api.entities.repositories.OrderItemRepository;
import com.ferme.itservices.api.entities.repositories.OrderRepository;
import com.ferme.itservices.api.entities.services.OrderService;
import com.ferme.itservices.order.utils.OrderAssertions;
import com.ferme.itservices.order.utils.OrderConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.order.utils.OrderConstants.ORDER_A_UUID;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ClientRepository clientRepository;

	@Mock
	private OrderItemRepository orderItemRepository;

	private final OrderConstants orderConstants = OrderConstants.getInstance();
	private final OrderAssertions orderAssertions = OrderAssertions.getInstance();

	@Test
	public void createOrder_WithValidData_ReturnsOrder() {
		final Order newOrder = orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS;
		final OrderDTO newOrderDTO = orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS_DTO;

		when(orderRepository.save(any(Order.class))).thenReturn(newOrder);

		final OrderDTO sut = orderService.create(newOrderDTO);

		orderAssertions.assertOrderProps(newOrderDTO, sut);
	}

	@Test
	public void createOrder_WithInvalidData_ThrowsException() {
		final Order invalidOrder = orderConstants.INVALID_ORDER;
		final OrderDTO invalidOrderDTO = orderConstants.INVALID_ORDER_DTO;

		when(orderRepository.save(invalidOrder)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> orderService.create(invalidOrderDTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateOrder_WithExistingOrder_ReturnsUpdatedOrder() {
		final Order order = orderConstants.ORDER;
		final OrderDTO newOrderDTO = orderConstants.ORDER_DTO;

		when(orderRepository.findById(order.getId())).thenReturn(of(order));
		when(orderRepository.save(order)).thenReturn(order);

		final OrderDTO updatedOrderDTO = orderService.update(order.getId(), newOrderDTO);

		orderAssertions.assertOrderProps(newOrderDTO, updatedOrderDTO);
	}

	@Test
	public void updateOrder_WithUnexistingOrder_ReturnsRecordNotFoundException() {
		final OrderDTO newOrderDTO = orderConstants.ORDER_DTO;

		assertThrows(RecordNotFoundException.class, () ->
			orderService.update(UUID.randomUUID(), newOrderDTO)
		);
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() {
		final Order order = orderConstants.ORDER;
		final OrderDTO orderDTO = orderConstants.ORDER_DTO;

		when(orderRepository.findById(ORDER_A_UUID)).thenReturn(of(order));

		final OrderDTO sut = orderService.findById(ORDER_A_UUID);

		orderAssertions.assertOrderProps(orderDTO, sut);
	}

	@Test
	public void getOrder_ByUnexistingId_ReturnsEmpty() {
		assertThatThrownBy(() -> orderService.findById(UUID.randomUUID())).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void listOrders_WhenOrdersExists_ReturnsAllOrders() {
		final List<Order> orders = orderConstants.ORDERS;
		final List<OrderDTO> ordersDTO = orderConstants.ORDERS_DTO;

		when(orderRepository.findAll()).thenReturn(orders);

		final List<OrderDTO> sut = orderService.listAll();

		orderAssertions.assertOrderListProps(ordersDTO, sut);
	}

	@Test
	public void listOrders_WhenOrdersDoesNotExists_ReturnsEmptyList() {
		when(orderRepository.findAll()).thenReturn(new ArrayList<>());

		final List<OrderDTO> sut = orderService.listAll();

		assertThat(sut).isEmpty();
	}

	@Test
	public void deleteOrder_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> orderService.deleteById(UUID.randomUUID())).doesNotThrowAnyException();
	}

	@Test
	public void deleteOrder_WithUnexistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(orderRepository).deleteById(UUID.randomUUID());

		assertThatThrownBy(() -> orderService.deleteById(UUID.randomUUID())).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void deleteAllOrders_doesNotThrowAnyException() {
		assertThatCode(() -> orderService.deleteAll()).doesNotThrowAnyException();
	}
}