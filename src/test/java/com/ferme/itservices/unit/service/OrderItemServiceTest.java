package com.ferme.itservices.unit.service;

import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.OrderItemRepository;
import com.ferme.itservices.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.common.OrderItemConstants.INVALID_ORDERITEM;
import static com.ferme.itservices.common.OrderItemConstants.ORDERITEM_A;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {
	@InjectMocks
	private OrderItemService orderItemService;

	@Mock
	private OrderItemRepository orderItemRepository;

	@Test
	public void createOrderItem_WithValidData_ReturnsClient() {
		when(orderItemRepository.save(ORDERITEM_A)).thenReturn(ORDERITEM_A);

		OrderItem sut = orderItemService.create(ORDERITEM_A);

		assertThat(sut).isEqualTo(ORDERITEM_A);
	}

	@Test
	public void createOrderItem_WithInvalidData_ThrowsException() {
		when(orderItemRepository.save(INVALID_ORDERITEM)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> orderItemService.create(INVALID_ORDERITEM)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() {
		when(orderItemRepository.findById(1L)).thenReturn(Optional.of(ORDERITEM_A));

		Optional<OrderItem> sut = orderItemService.findById(1L);

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(ORDERITEM_A);
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsOrderItem() {
		when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

		Optional<OrderItem> sut = orderItemService.findById(1L);

		assertThat(sut).isEmpty();
	}

	@Test
	public void listOrderItems_ReturnsAllOrderItems() {
		List<OrderItem> orderItems = new ArrayList<>() {
			{ add(ORDERITEM_A); }
		};
		when(orderItemRepository.findAll()).thenReturn(orderItems);

		List<OrderItem> sut = orderItemRepository.findAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(1);
		assertThat(sut.getFirst()).isEqualTo(ORDERITEM_A);
	}

	@Test
	public void listOrderItems_ReturnsNoOrderItems() {
		when(orderItemRepository.findAll()).thenReturn(Collections.emptyList());

		List<OrderItem> sut = orderItemRepository.findAll();

		assertThat(sut).isEmpty();
	}

	@Test
	public void deleteOrderItem_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> orderItemService.deleteById(1L)).doesNotThrowAnyException();
	}

	@Test
	public void deleteOrderItem_WithUnexistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(orderItemRepository).deleteById(1L);

		assertThatThrownBy(() -> orderItemService.deleteById(1L)).isInstanceOf(RuntimeException.class);
	}
}