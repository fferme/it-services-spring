package com.ferme.itservices.order;

import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.Order;
import com.ferme.itservices.repositories.OrderRepository;
import com.ferme.itservices.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.client.ClientConstants.FELIPE;
import static com.ferme.itservices.order.OrderConstants.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Test
	public void createOrder_WithValidData_ReturnsOrder() {
		when(orderRepository.save(ORDER_A)).thenReturn(ORDER_A);

		Order sut = orderService.create(ORDER_A);

		assertThat(sut).isEqualTo(ORDER_A);
	}

	@Test
	public void createOrder_WithInvalidData_ThrowsException() {
		when(orderRepository.save(INVALID_ORDER)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> orderService.create(INVALID_ORDER)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateOrder_WithExistingOrder_ReturnsUpdatedOrder() {
		when(orderRepository.findById(ORDER_A.getId())).thenReturn(of(ORDER_A));
		when(orderRepository.save(ORDER_A)).thenReturn(ORDER_A);

		Order updatedOrder = orderService.update(FELIPE.getId(), NEW_ORDER);

		assertEquals(NEW_ORDER.getDeviceName(), updatedOrder.getDeviceName());
		assertEquals(NEW_ORDER.getDeviceSN(), updatedOrder.getDeviceSN());
		assertEquals(NEW_ORDER.getProblems(), updatedOrder.getProblems());
		assertEquals(NEW_ORDER.getClient(), updatedOrder.getClient());
		assertEquals(NEW_ORDER.getOrderItems(), updatedOrder.getOrderItems());
	}

	@Test
	public void updateOrder_WithUnexistingOrder_ReturnsRecordNotFoundException() {
		when(orderRepository.findById(9L)).thenReturn(empty());

		assertThrows(RecordNotFoundException.class, () -> {
			orderService.update(9L, NEW_ORDER);
		});
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() {
		when(orderRepository.findById(1L)).thenReturn(Optional.of(ORDER_A));

		Optional<Order> sut = orderService.findById(1L);

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(ORDER_A);
	}

	@Test
	public void getOrder_ByUnexistingId_ReturnsEmpty() {
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());

		Optional<Order> sut = orderService.findById(1L);

		assertThat(sut).isEmpty();
	}

	@Test
	public void listOrders_WhenOrdersExists_ReturnsAllOrders() {
		when(orderRepository.findAll()).thenReturn(ORDERS);

		List<Order> sut = orderRepository.findAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(ORDERS.size());
		assertThat(sut.get(0)).isEqualTo(ORDER_A);
		assertThat(sut.get(1)).isEqualTo(ORDER_B);
		assertThat(sut.get(2)).isEqualTo(ORDER_C);
	}

	@Test
	public void listOrders_WhenOrdersDoesNotExists_ReturnsEmptyList() {
		when(orderRepository.findAll()).thenReturn(Collections.emptyList());

		List<Order> sut = orderRepository.findAll();

		assertThat(sut).isEmpty();
	}

	@Test
	public void deleteOrder_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> orderService.deleteById(1L)).doesNotThrowAnyException();
	}

	@Test
	public void deleteOrder_WithUnexistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(orderRepository).deleteById(1L);

		assertThatThrownBy(() -> orderService.deleteById(1L)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void deleteAllOrders_doesNotThrowAnyException() {
		assertThatCode(() -> orderService.deleteAll()).doesNotThrowAnyException();
	}
}