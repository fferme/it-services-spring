package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.OrderDTO;
import com.ferme.itservices.api.models.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderDTO toDTO(Order order) {
        return (order == null)
            ? null
            : OrderDTO.builder()
                      .id(order.getId())
                      .header(order.getHeader())
                      .deviceName(order.getDeviceName())
                      .deviceSN(order.getDeviceSN())
                      .problems(order.getProblems())
                      .createdAt(order.getCreatedAt())
                      .updatedAt(order.getUpdatedAt())
                      .build();
    }

    public Order toEntity(OrderDTO orderDTO) {
        return (orderDTO == null)
            ? null
            : Order.builder()
                   .id(orderDTO.getId())
                   .header(orderDTO.getHeader())
                   .deviceName(orderDTO.getDeviceName())
                   .deviceSN(orderDTO.getDeviceSN())
                   .problems(orderDTO.getProblems())
                   .createdAt(orderDTO.getCreatedAt())
                   .updatedAt(orderDTO.getUpdatedAt())
                   .build();
    }
}