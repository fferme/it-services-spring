package com.ferme.itservices.order;

import com.ferme.itservices.dtos.OrderDTO;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.Order;
import com.ferme.itservices.order.utils.OrderAssertions;
import com.ferme.itservices.order.utils.OrderConstants;
import com.ferme.itservices.repositories.OrderRepository;
import com.ferme.itservices.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.dtos.mappers.OrderMapper.toOrderDTO;
import static com.ferme.itservices.dtos.mappers.OrderMapper.toOrderDTOList;
import static com.ferme.itservices.order.utils.OrderConstants.ORDER_A_UUID;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	private final OrderConstants orderConstants = OrderConstants.getInstance();
	private final OrderAssertions orderAssertions = OrderAssertions.getInstance();

	@Test
	public void createOrder_WithValidData_ReturnsOrder() {
		final Order newOrder = orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS;
		final OrderDTO newOrderDTO = toOrderDTO(newOrder);

		when(orderRepository.save(newOrder)).thenReturn(newOrder);

		OrderDTO sut = orderService.create(newOrderDTO);

		orderAssertions.assertOrderProps(newOrderDTO, sut);
	}

	@Test
	public void createOrder_WithInvalidData_ThrowsException() {
		final Order emptyOrder = orderConstants.EMPTY_ORDER;
		final OrderDTO emptyOrderDTO = toOrderDTO(emptyOrder);

		final Order invalidOrder = orderConstants.INVALID_ORDER;
		final OrderDTO invalidOrderDTO = toOrderDTO(invalidOrder);

		when(orderRepository.save(emptyOrder)).thenThrow(RuntimeException.class);
		when(orderRepository.save(invalidOrder)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> orderService.create(emptyOrderDTO)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> orderService.create(invalidOrderDTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateOrder_WithExistingOrder_ReturnsUpdatedOrder() {
		final Order order = orderConstants.ORDER;
		final OrderDTO newOrderDTO = toOrderDTO(order);

		when(orderRepository.findById(order.getId())).thenReturn(of(order));
		when(orderRepository.save(order)).thenReturn(order);

		OrderDTO updatedOrderDTO = orderService.update(order.getId(), newOrderDTO);

		orderAssertions.assertOrderProps(newOrderDTO, updatedOrderDTO);
	}

	@Test
	public void updateOrder_WithUnexistingOrder_ReturnsRecordNotFoundException() {
		final OrderDTO newOrderDTO = toOrderDTO(orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS);

		assertThrows(RecordNotFoundException.class, () ->
			orderService.update(UUID.randomUUID(), newOrderDTO)
		);
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() {
		final Order order = orderConstants.ORDER;
		final OrderDTO orderDTO = toOrderDTO(order);

		when(orderRepository.findById(ORDER_A_UUID)).thenReturn(of(order));

		OrderDTO sut = orderService.findById(ORDER_A_UUID);

		orderAssertions.assertOrderProps(orderDTO, sut);
	}

	@Test
	public void getOrder_ByUnexistingId_ReturnsEmpty() {
		assertThatThrownBy(() -> orderService.findById(UUID.randomUUID())).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void listOrders_WhenOrdersExists_ReturnsAllOrders() {
		final List<Order> orders = orderConstants.ORDERS;
		final List<OrderDTO> ordersDTO = toOrderDTOList(orders);

		when(orderRepository.findAll()).thenReturn(orders);

		List<OrderDTO> sut = orderService.listAll();

		orderAssertions.assertOrderListProps(ordersDTO, sut);
	}

	@Test
	public void listOrders_WhenOrdersDoesNotExists_ReturnsEmptyList() {
		when(orderRepository.findAll()).thenReturn(new ArrayList<>());

		List<OrderDTO> sut = orderService.listAll();

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