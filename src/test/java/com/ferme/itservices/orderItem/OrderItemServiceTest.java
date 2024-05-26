package com.ferme.itservices.orderItem;

import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.repositories.OrderItemRepository;
import com.ferme.itservices.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.ferme.itservices.orderItem.OrderItemConstants.*;
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

		OrderItemDTO sut = orderItemService.create(ORDERITEM_A_DTO);

		assertThat(sut.orderItemType()).isEqualTo(ORDERITEM_A_DTO.orderItemType());
		assertThat(sut.description()).isEqualTo(ORDERITEM_A_DTO.description());
		assertThat(sut.price()).isEqualTo(ORDERITEM_A_DTO.price());
	}

	@Test
	public void createOrderItem_WithInvalidData_ThrowsException() {
		when(orderItemRepository.save(EMPTY_ORDERITEM)).thenThrow(RuntimeException.class);
		when(orderItemRepository.save(INVALID_ORDERITEM)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> orderItemService.create(EMPTY_ORDERITEM_DTO)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> orderItemService.create(INVALID_ORDERITEM_DTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateOrderItem_WithExistingOrderItem_ReturnsUpdatedOrderItem() {
		when(orderItemRepository.findById(ORDERITEM_WITH_ID.getId())).thenReturn(Optional.of(ORDERITEM_WITH_ID));
		when(orderItemRepository.save(ORDERITEM_WITH_ID)).thenReturn(ORDERITEM_WITH_ID);

		OrderItemDTO updatedOrderItemDTO = orderItemService.update(ORDERITEM_WITH_ID.getId(), NEW_ORDERITEM_DTO);

		assertEquals(NEW_ORDERITEM_DTO.orderItemType(), updatedOrderItemDTO.orderItemType());
		assertEquals(NEW_ORDERITEM_DTO.description(), updatedOrderItemDTO.description());
		assertEquals(NEW_ORDERITEM_DTO.price(), updatedOrderItemDTO.price());
	}

	@Test
	public void updateOrderItem_WithUnexistingOrderItem_ReturnsRecordNotFoundException() {
		assertThrows(RecordNotFoundException.class, () -> {
			orderItemService.update(UUID.randomUUID(), NEW_ORDERITEM_DTO);
		});
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() {
		when(orderItemRepository.findById(ORDERITEM_A_UUID)).thenReturn(Optional.of(ORDERITEM_A));

		OrderItemDTO sut = orderItemService.findById(ORDERITEM_A_UUID);

		assertThat(sut).isNotNull();
		assertThat(sut).isEqualTo(ORDERITEM_A_DTO);
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsOrderItem() {
		assertThatThrownBy(() -> orderItemService.findById(UUID.randomUUID())).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void listOrderItems_WhenOrderItemExists_ReturnsAllOrderItemsSortedByDescription() {
		when(orderItemRepository.findAll()).thenReturn(ORDER_ITEMS);

		List<OrderItemDTO> sut = orderItemService.listAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(ORDER_ITEMS.size());
		assertThat(sut.get(0)).isEqualTo(ORDERITEM_A_DTO);
		assertThat(sut.get(1)).isEqualTo(ORDERITEM_B_DTO);
		assertThat(sut.get(2)).isEqualTo(ORDERITEM_C_DTO);
		assertThat(sut).isSortedAccordingTo(Comparator.comparing(OrderItemDTO::description));
	}

	@Test
	public void listOrderItems_WhenOrderItemsDoesNotExists_ReturnsNoOrderItems() {
		when(orderItemRepository.findAll()).thenReturn(new ArrayList<>());

		List<OrderItemDTO> sut = orderItemService.listAll();

		assertThat(sut).isEmpty();
	}

	@Test
	public void deleteOrderItem_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> orderItemService.deleteById(UUID.randomUUID())).doesNotThrowAnyException();
	}

	@Test
	public void deleteOrderItem_WithUnexistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(orderItemRepository).deleteById(UUID.randomUUID());

		assertThatThrownBy(() -> orderItemService.deleteById(UUID.randomUUID())).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void deleteAllOrderItems_doesNotThrowAnyException() {
		assertThatCode(() -> orderItemService.deleteAll()).doesNotThrowAnyException();
	}
}