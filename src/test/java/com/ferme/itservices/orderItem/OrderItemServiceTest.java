package com.ferme.itservices.orderItem;

import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.OrderItemRepository;
import com.ferme.itservices.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.orderItem.OrderItemConstants.*;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
	public void updateOrderItem_WithExistingOrderItem_ReturnsUpdatedOrderItem() {
		when(orderItemRepository.findById(ORDERITEM_A.getId())).thenReturn(java.util.Optional.of(ORDERITEM_A));
		when(orderItemRepository.save(ORDERITEM_A)).thenReturn(ORDERITEM_A);

		OrderItem updatedOrderItem = orderItemService.update(ORDERITEM_A.getId(), NEW_ORDERITEM_A);

		assertEquals(NEW_ORDERITEM_A.getOrderItemType(), updatedOrderItem.getOrderItemType());
		assertEquals(NEW_ORDERITEM_A.getDescription(), updatedOrderItem.getDescription());
		assertEquals(NEW_ORDERITEM_A.getPrice(), updatedOrderItem.getPrice());
	}

	@Test
	public void updateOrderItem_WithUnexistingOrderItem_ReturnsRecordNotFoundException() {
		when(orderItemRepository.findById(9L)).thenReturn(empty());

		assertThrows(RecordNotFoundException.class, () -> {
			orderItemService.update(9L, NEW_ORDERITEM_A);
		});
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
	public void listOrderItems_WhenOrderItemExists_ReturnsAllOrderItemsSortedByDescription() {
		when(orderItemRepository.findAll()).thenReturn(ORDER_ITEMS);

		List<OrderItem> sut = orderItemService.listAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(ORDER_ITEMS.size());
		assertThat(sut.get(0)).isEqualTo(ORDERITEM_C);
		assertThat(sut.get(1)).isEqualTo(ORDERITEM_B);
		assertThat(sut.get(2)).isEqualTo(ORDERITEM_A);
		assertThat(sut).isSortedAccordingTo(Comparator.comparing(OrderItem::getDescription));
	}

	@Test
	public void listOrderItems_ReturnsSortedOrderItemsByDescription() {
		when(orderItemRepository.findAll()).thenReturn(ORDER_ITEMS);

		List<OrderItem> sut = orderItemService.listAll();

		assertThat(sut).isSortedAccordingTo(Comparator.comparing(OrderItem::getDescription));
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

	@Test
	public void deleteAllOrderItems_doesNotThrowAnyException() {
		assertThatCode(() -> orderItemService.deleteAll()).doesNotThrowAnyException();
	}
}