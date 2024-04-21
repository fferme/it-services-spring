package com.ferme.itservices.unit.service;

import com.ferme.itservices.models.Order;
import com.ferme.itservices.repositories.OrderRepository;
import com.ferme.itservices.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.common.OrderConstants.INVALID_ORDER;
import static com.ferme.itservices.common.OrderConstants.VALID_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
		when(orderRepository.save(VALID_ORDER)).thenReturn(VALID_ORDER);

		Order sut = orderService.create(VALID_ORDER);

		assertThat(sut).isEqualTo(VALID_ORDER);
	}

	@Test
	public void createOrder_WithInvalidData_ThrowsException() {
		when(orderRepository.save(INVALID_ORDER)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> orderService.create(INVALID_ORDER)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() {
		when(orderRepository.findById(1L)).thenReturn(Optional.of(VALID_ORDER));

		Optional<Order> sut = orderService.findById(1L);

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(VALID_ORDER);
	}

	@Test
	public void getOrder_ByUnexistingId_ReturnsOrder() {
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());

		Optional<Order> sut = orderService.findById(1L);

		assertThat(sut).isEmpty();
	}

	@Test
	public void listOrders_ReturnsAllOrders() {
		List<Order> orders = new ArrayList<>() {
			{ add(VALID_ORDER); }
		};
		when(orderRepository.findAll()).thenReturn(orders);

		List<Order> sut = orderRepository.findAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(1);
		assertThat(sut.getFirst()).isEqualTo(VALID_ORDER);
	}

	@Test
	public void listOrders_ReturnsNoOrders() {
		when(orderRepository.findAll()).thenReturn(Collections.emptyList());

		List<Order> sut = orderRepository.findAll();

		assertThat(sut).isEmpty();
	}

	@Test
	public void deleteOrder_WithUnexistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(orderRepository).deleteById(1L);

		assertThatThrownBy(() -> orderService.deleteById(1L)).isInstanceOf(RuntimeException.class);
	}
}