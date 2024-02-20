package com.ferme.itservices.api.services;

import com.ferme.itservices.api.dtos.OrderDTO;
import com.ferme.itservices.api.dtos.mappers.OrderMapper;
import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.repositories.OrderRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDTO> listAll() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                      .map(orderMapper::toDTO)
                      .sorted(Comparator.comparing(OrderDTO::getCreatedAt))
                      .collect(Collectors.toList());
    }

    public OrderDTO findById(@Valid @NotNull UUID id) {
        return orderRepository.findById(id).map(orderMapper::toDTO)
                              .orElseThrow(() -> new RecordNotFoundException(Order.class, id));
    }

    public OrderDTO create(@Valid @NotNull OrderDTO OrderDTO) {
        return orderMapper.toDTO(orderRepository.save(orderMapper.toEntity(OrderDTO)));
    }

    public OrderDTO update(@NotNull UUID id, @Valid @NotNull OrderDTO newOrderDTO) {
        return orderRepository.findById(id)
                               .map(orderFound -> {
                                   orderFound.setDeviceName(newOrderDTO.getDeviceName());
                                   orderFound.setDeviceSN(newOrderDTO.getDeviceSN());
                                   orderFound.setProblems(newOrderDTO.getProblems());

                                   return orderMapper.toDTO(orderRepository.save(orderFound));

                               }).orElseThrow(() -> new RecordNotFoundException(Order.class, id));
    }

    public void deleteById(@NotNull UUID id) {
        orderRepository.delete(orderRepository.findById(id)
                                              .orElseThrow(() -> new RecordNotFoundException(Order.class, id)));
    }

    public void deleteAll() {
        orderRepository.deleteAll();
    }

}