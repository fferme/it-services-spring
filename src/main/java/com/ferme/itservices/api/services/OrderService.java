package com.ferme.itservices.api.services;

import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.repositories.OrderRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    public Order findById(@Valid @NotNull UUID id) {
        return orderRepository.findById(id)
                              .orElseThrow(() -> new RecordNotFoundException(Order.class, id));
    }

    public Order create(@Valid @NotNull Order order) {
        return orderRepository.save(order);
    }

    public Order update(@NotNull UUID id, @Valid @NotNull Order updatedOrder) {
        Order existingOrder = findById(id);
        existingOrder.setDeviceName(updatedOrder.getDeviceName());
        existingOrder.setDeviceSN(updatedOrder.getDeviceSN());
        existingOrder.setProblems(updatedOrder.getProblems());
        return orderRepository.save(existingOrder);
    }

    public void deleteById(@NotNull UUID id) {
        orderRepository.deleteById(id);
    }

    public void deleteAll() {
        orderRepository.deleteAll();
    }
}