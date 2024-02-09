package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.api.models.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemDTO orderItemDTO);
    OrderItemDTO toDTO(OrderItem orderItem);
    List<OrderItemDTO> toDTOList(List<OrderItem> orderItemList);
}