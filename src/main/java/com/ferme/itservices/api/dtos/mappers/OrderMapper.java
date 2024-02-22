package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.OrderDTO;
import com.ferme.itservices.api.models.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderDTO toDTO(Order order) {
        if (order == null) { return null; }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setHeader(order.getHeader());
        orderDTO.setDeviceName(order.getDeviceName());
        orderDTO.setDeviceSN(order.getDeviceSN());
        orderDTO.setProblems(order.getProblems());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());

        return orderDTO;
    }

    public Order toEntity(OrderDTO orderDTO) {

        if (orderDTO == null) {
            return null;
        }

        Order order = new Order();
        if (orderDTO.getId() != null) {
            order.setId(orderDTO.getId());
        }
        order.setId(orderDTO.getId());
        order.setCreatedAt(orderDTO.getCreatedAt());
        order.setUpdatedAt(orderDTO.getUpdatedAt());
        order.setDeviceName(orderDTO.getDeviceName());
        order.setDeviceSN(orderDTO.getDeviceSN());
        order.setProblems(orderDTO.getProblems());
        order.setCreatedAt(orderDTO.getCreatedAt());
        order.setUpdatedAt(orderDTO.getUpdatedAt());

        return order;
    }
}