package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.api.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = OrderItemTypeConverter.class)
public interface OrderItemMapper {
    @Mapping(target = "orderItemType",
             expression = "java(orderItemDTO.getOrderItemType() != null ? OrderItemTypeConverter.convertOrderItemTypeValue(orderItemDTO.getOrderItemType()) : null)"
    )
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    @Mapping(target = "orderItemType",
             expression = "java(orderItem.getOrderItemType() != null ? orderItem.getOrderItemType().getValue() : null)"
    )
    OrderItemDTO toDTO(OrderItem orderItem);
    List<OrderItemDTO> toDTOList(List<OrderItem> orderItemList);
}