package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.api.models.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {
    public OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO) {
        return (orderItemDTO == null)
            ? null
            : OrderItem.builder()
                        .id(orderItemDTO.getId())
                        .orderItemType(
                            OrderItemTypeConverter.convertOrderItemTypeValue(orderItemDTO.getOrderItemType()))
                        .description(orderItemDTO.getDescription())
                        .cashPrice(orderItemDTO.getCashPrice())
                        .installmentPrice(orderItemDTO.getInstallmentPrice())
                        .createdAt(orderItemDTO.getCreatedAt())
                        .updatedAt(orderItemDTO.getUpdatedAt())
                        .build();
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return (orderItem == null)
            ? null
            : OrderItemDTO.builder()
                           .id(orderItem.getId())
                           .orderItemType(orderItem.getOrderItemType().getValue())
                           .description(orderItem.getDescription())
                           .cashPrice(orderItem.getCashPrice())
                           .installmentPrice(orderItem.getInstallmentPrice())
                           .createdAt(orderItem.getCreatedAt())
                           .updatedAt(orderItem.getUpdatedAt())
                           .build();
    }

    public List<OrderItem> toOrderItemListEntity(List<OrderItemDTO> orderItemsDTOList) {
        return orderItemsDTOList
            .stream()
            .map(this::toOrderItemEntity).collect(Collectors.toList());
    }

    public List<OrderItemDTO> toOrderItemListDTO(List<OrderItem> orderItemsList) {
        return orderItemsList
            .stream()
            .map(this::toOrderItemDTO).collect(Collectors.toList());
    }
}