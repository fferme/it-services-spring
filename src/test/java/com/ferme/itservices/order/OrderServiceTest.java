package com.ferme.itservices.order;

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
import static com.ferme.itservices.common.OrderConstants.ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    public void createOrder_WithValidData_ReturnsOrder() {
        when(orderRepository.save(ORDER)).thenReturn(ORDER);

        Order sut = orderService.create(ORDER);

        assertThat(sut).isEqualTo(ORDER);
    }

    @Test
    public void createOrder_WithInvalidData_ThrowsException() {
        when(orderRepository.save(INVALID_ORDER)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> orderService.create(INVALID_ORDER)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getOrder_ByExistingId_ReturnsOrder() {
        UUID uuid = UUID.randomUUID();
        when(orderRepository.findById(uuid)).thenReturn(Optional.of(ORDER));

        Optional<Order> sut = orderService.findById(uuid);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(ORDER);
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
            { add(ORDER); }
        };
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> sut = orderRepository.findAll();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.getFirst()).isEqualTo(ORDER);
    }

    @Test
    public void listOrders_ReturnsNoOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<Order> sut = orderRepository.findAll();

        assertThat(sut).isEmpty();
    }

    @Test
    public void deleteOrder_WithExistingId_doesNotThrowAnyException(