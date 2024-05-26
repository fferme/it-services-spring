package com.ferme.itservices.dtos.mappers;

import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {
	public static OrderItem toOrderItem(OrderItemDTO orderItemDTO) {
		return new OrderItem(
			orderItemDTO.id(),
			orderItemDTO.orderItemType(),
			orderItemDTO.description(),
			orderItemDTO.price(),
			orderItemDTO.orders()
		);
	}

	public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
		return new OrderItemDTO(
			orderItem.getId(),
			orderItem.getOrderItemType(),
			orderItem.getDescription(),
			orderItem.getPrice(),
			orderItem.getOrders()
		);
	}

	public static List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> orderItems) {
		List<OrderItemDTO> dtos = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			dtos.add(toOrderItemDTO(orderItem));
		}
		return dtos;
	}
}
