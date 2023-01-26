package com.fermesolutions.itservices.service;

import com.fermesolutions.itservices.exception.RecordNotFoundException;
import com.fermesolutions.itservices.model.OrderItem;
import com.fermesolutions.itservices.repository.OrderItemRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> listAll() {
        return orderItemRepository.findAll();
    }

    public OrderItem findById(@PathVariable @NotNull @Positive Long id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public OrderItem create(@Valid OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public OrderItem update(@NotNull @Positive Long id, @Valid OrderItem newOrderItem) {
        return orderItemRepository.findById(id)
                .map(orderItemFound -> {
                    orderItemFound.setOrderItemType(newOrderItem.getOrderItemType());
                    orderItemFound.setDescription(newOrderItem.getDescription());
                    orderItemFound.setPrice(newOrderItem.getPrice());

                    return orderItemRepository.save(orderItemFound);
                }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public void delete(@PathVariable @NotNull @Positive Long id) {
        orderItemRepository.delete(orderItemRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException(id)));
    }


}
