package com.ferme.itservices.orderItem;

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
import static com.ferme.itservices.common.OrderItemConstants.ORDERITEM;
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
        when(orderItemRepository.save(ORDERITEM)).thenReturn(ORDERITEM);

        OrderItem sut = orderItemService.create(ORDERITEM);

        assertThat(sut).isEqualTo(ORDERITEM);
    }

    @Test
    public void createOrderItem_WithInvalidData_ThrowsException() {
        when(orderItemRepository.save(INVALID_ORDERITEM)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> orderItemService.create(INVALID_ORDERITEM)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getOrderItem_ByExistingId_ReturnsOrderItem() {
        UUID uuid = UUID.randomUUID();
        when(orderItemRepository.findById(uuid)).thenReturn(Optional.of(ORDERITEM));

        Optional<OrderItem> sut = orderItemService.findById(uuid);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(ORDERITEM);
    }

    @Test
    public void getOrderItem_ByUnexistingId_ReturnsOrderItem() {
        UUID uuid = UUID.randomUUID();
        when(orderItemRepository.findById(uuid)).thenReturn(Optional.empty());

        Optional<OrderItem> sut = orderItemService.findById(uuid);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listOrderItems_ReturnsAllOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>() {
            { add(ORDERITEM); }
        };
        when(orderItemRepository.findAll()).thenReturn(orderItems);

        List<OrderItem> sut = orderItemRepository.findAll();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.getFirst()).isEqualTo(ORDERITEM);
    }

    @Test
    public void listOrderItems_ReturnsNoOrderItems() {
        when(orderItemRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderItem> sut = orderItemRepository.findAll();

        assertThat(sut).isEmpty();
    }

    @Test
    public void deleteOrderItem_WithExistingId_doesNotThrowAnyException() {
        UUID uuid = UUID.randomUUID();
        assertThatCode(() -> orderItemService.deleteById(uuid)).doesNotThrowAnyException();
    }

    @Test
    public void deleteOrderItem_WithUnexistingId_ThrowsException() {
        UUID unexistingUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        doThrow(new RuntimeException()).when(orderItemRepository).deleteById(unexistingUUID);

        assertThatThrownBy(() -> orderItemService.deleteById(unexistingUUID)).isInstanceOf(RuntimeException.class);
    }
}