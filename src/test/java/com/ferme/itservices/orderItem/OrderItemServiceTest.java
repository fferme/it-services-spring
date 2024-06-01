package com.ferme.itservices.orderItem;

import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.orderItem.utils.OrderItemAssertions;
import com.ferme.itservices.orderItem.utils.OrderItemConstants;
import com.ferme.itservices.repositories.OrderItemRepository;
import com.ferme.itservices.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ferme.itservices.dtos.mappers.OrderItemMapper.toOrderItemDTO;
import static com.ferme.itservices.dtos.mappers.OrderItemMapper.toOrderItemDTOList;
import static com.ferme.itservices.orderItem.utils.OrderItemConstants.ORDERITEM_A_UUID;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {
	@InjectMocks
	private OrderItemService orderItemService;

	@Mock
	private OrderItemRepository orderItemRepository;

	private static final OrderItemConstants orderItemConstants = OrderItemConstants.getInstance();
	private static final OrderItemAssertions orderItemAssertions = OrderItemAssertions.getInstance();

	@Test
	public void createOrderItem_WithValidData_ReturnsClient() {
		final OrderItem orderItem = orderItemConstants.ORDERITEM;
		final OrderItemDTO orderItemDTO = toOrderItemDTO(orderItem);

		when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

		OrderItemDTO sut = orderItemService.create(orderItemDTO);

		orderItemAssertions.assertOrderItemProps(orderItemDTO, sut);
	}

	@Test
	public void createOrderItem_WithInvalidData_ThrowsException() {
		final OrderItem emptyOrderItem = orderItemConstants.EMPTY_ORDERITEM;
		final OrderItemDTO emptyOrderItemDTO = toOrderItemDTO(emptyOrderItem);

		final OrderItem invalidOrderItem = orderItemConstants.INVALID_ORDERITEM;
		final OrderItemDTO invalidOrderItemDTO = toOrderItemDTO(invalidOrderItem);

		when(orderItemRepository.save(emptyOrderItem)).thenThrow(RuntimeException.class);
		when(orderItemRepository.save(invalidOrderItem)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> orderItemService.create(emptyOrderItemDTO)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> orderItemService.create(invalidOrderItemDTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateOrderItem_WithExistingOrderItem_ReturnsUpdatedOrderItem() {
		final OrderItem orderItem = orderItemConstants.ORDERITEM;
		final OrderItemDTO newOrderItemDTO = toOrderItemDTO(orderItemConstants.NEW_ORDERITEM);

		when(orderItemRepository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
		when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

		OrderItemDTO updatedOrderItemDTO = orderItemService.update(orderItem.getId(), newOrderItemDTO);

		orderItemAssertions.assertOrderItemProps(updatedOrderItemDTO, newOrderItemDTO);
	}

	@Test
	public void updateOrderItem_WithUnexistingOrderItem_ReturnsRecordNotFoundException() {
		final OrderItemDTO newOrderItemDTO = toOrderItemDTO(orderItemConstants.NEW_ORDERITEM);

		assertThrows(RecordNotFoundException.class, () -> {
			orderItemService.update(UUID.randomUUID(), newOrderItemDTO);
		});
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() {
		final OrderItem orderItem = orderItemConstants.ORDERITEM;
		final OrderItemDTO orderItemDTO = toOrderItemDTO(orderItem);

		when(orderItemRepository.findById(ORDERITEM_A_UUID)).thenReturn(Optional.of(orderItem));

		OrderItemDTO sut = orderItemService.findById(ORDERITEM_A_UUID);

		orderItemAssertions.assertOrderItemProps(orderItemDTO, sut);
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsOrderItem() {
		assertThatThrownBy(() -> orderItemService.findById(UUID.randomUUID())).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void listOrderItems_WhenOrderItemExists_ReturnsAllOrderItemsSortedByDescription() {
		final List<OrderItem> orderItems = orderItemConstants.ORDER_ITEMS;
		final List<OrderItemDTO> orderItemsDTO = toOrderItemDTOList(orderItems);

		when(orderItemRepository.findAll()).thenReturn(orderItems);

		List<OrderItemDTO> sut = orderItemService.listAll();


		orderItemAssertions.assertOrderItemListProps(orderItemsDTO, sut);
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