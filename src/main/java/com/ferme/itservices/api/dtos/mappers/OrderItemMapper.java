package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.api.models.OrderItem;

public interface OrderItemMapper {
    OrderItem toEntity(OrderItemDTO orderItemDTO);
    OrderItemDTO toDTO(OrderItem orderItem);
}