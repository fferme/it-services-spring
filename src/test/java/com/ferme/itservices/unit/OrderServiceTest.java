package com.ferme.itservices.unit;

import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.repositories.OrderRepository;
import com.ferme.itservices.api.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
        UUID uuid = UUID.randomUUID();
        when(orderRepository.findById(uuid)).thenReturn(Optional.of(VALID_ORDER));

        Optional<Order> sut = orderService.findById(uuid);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(VALID_ORDER);
    }

    @Test
    public void getOrder_ByUnexistingId_ReturnsOrder() {
        UUID uuid = UUID.randomUUID();
        when(orderRepository.findById(uuid)).thenReturn(Optional.empty());

        Optional<Order> sut = orderService.findById(uuid);

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
        UUID unexistingUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        doThrow(new RuntimeException()).when(orderRepository).deleteById(unexistingUUID);

        assertThatThrownBy(() -> orderService.deleteById(unexistingUUID)).isInstanceOf(RuntimeException.class);
    }
}