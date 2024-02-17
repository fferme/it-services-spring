package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.OrderDTO;
import com.ferme.itservices.api.models.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderDTO orderDTO);
    OrderDTO toDTO(Order order);
    List<OrderDTO> toDTOList(List<Order> orderList);
}