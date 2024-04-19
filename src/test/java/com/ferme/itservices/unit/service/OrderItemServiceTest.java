package com.ferme.itservices.unit.service;

import com.ferme.itservices.api.models.OrderItem;
import com.ferme.itservices.api.repositories.OrderItemRepository;
import com.ferme.itservices.api.services.OrderItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.ferme.itservices.common.OrderItemConstants.INVALID_ORDERITEM;
import static com.ferme.itservices.common.OrderItemConstants.VALID_ORDERITEM;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {
    @InjectMocks
    private OrderItemService orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    private static final Long valid_id = 123456L;
    private static final Long invalid_id = 978541L;

    @Test
    public void createOrderItem_WithValidData_ReturnsClient() {
        when(orderItemRepository.save(VALID_ORDERITEM)).thenReturn(VALID_ORDERITEM);

        OrderItem sut = orderItemService.create(VALID_ORDERITEM);

        assertThat(sut).isEqualTo(VALID_ORDERITEM);
    }

    @Test
    public void createOrderItem_WithInvalidData_ThrowsException() {
        when(orderItemRepository.save(INVALID_ORDERITEM)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> orderItemService.create(INVALID_ORDERITEM)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getOrderItem_ByExistingId_ReturnsOrderItem() {
        when(orderItemRepository.findById(valid_id)).thenReturn(Optional.of(VALID_ORDERITEM));

        Optional<OrderItem> sut = orderItemService.findById(valid_id);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(VALID_ORDERITEM);
    }

    @Test
    public void getOrderItem_ByUnexistingId_ReturnsOrderItem() {
        when(orderItemRepository.findById(invalid_id)).thenReturn(Optional.empty());

        Optional<OrderItem> sut = orderItemService.findById(invalid_id);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listOrderItems_ReturnsAllOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>() {
            { add(VALID_ORDERITEM); }
        };
        when(orderItemRepository.findAll()).thenReturn(orderItems);

        List<OrderItem> sut = orderItemRepository.findAll();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.getFirst()).isEqualTo(VALID_ORDERITEM);
    }

    @Test
    public void listOrderItems_ReturnsNoOrderItems() {
        when(orderItemRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderItem> sut = orderItemRepository.findAll();

        assertThat(sut).isEmpty();
    }

    @Test
    public void deleteOrderItem_WithExistingId_doesNotThrowAnyException() {
        assertThatCode(() -> orderItemService.deleteById(valid_id)).doesNotThrowAnyException();
    }

    @Test
    public void deleteOrderItem_WithUnexistingId_ThrowsException() {
        doThrow(new RuntimeException()).when(orderItemRepository).deleteById(invalid_id);

        assertThatThrownBy(() -> orderItemService.deleteById(invalid_id)).isInstanceOf(RuntimeException.class);
    }
}