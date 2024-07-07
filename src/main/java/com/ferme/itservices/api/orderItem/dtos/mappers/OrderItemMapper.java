package com.ferme.itservices.api.orderItem.dtos.mappers;

import com.ferme.itservices.api.orderItem.dtos.OrderItemDTO;
import com.ferme.itservices.api.orderItem.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {
	public static OrderItem toOrderItem(OrderItemDTO orderItemDTO) {
		return new OrderItem(
			orderItemDTO.id(),
			orderItemDTO.orderItemType(),
			orderItemDTO.description(),
			orderItemDTO.price(),
			orderItemDTO.showInListAll(),
			orderItemDTO.quantity(),
			orderItemDTO.orders(),
			orderItemDTO.auditInfo()
		);
	}

	public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
		return new OrderItemDTO(
			orderItem.getId(),
			orderItem.getOrderItemType(),
			orderItem.getDescription(),
			orderItem.getPrice(),
			orderItem.getShowInListAll(),
			orderItem.getQuantity(),
			orderItem.getOrders(),
			orderItem.getAuditInfo()
		);
	}

	public static List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> orderItems) {
		List<OrderItemDTO> dtos = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			dtos.add(toOrderItemDTO(orderItem));
		}
		return dtos;
	}

	public static List<OrderItem> toOrderItemList(List<OrderItemDTO> orderItemsDTO) {
		List<OrderItem> orderItems = new ArrayList<>();
		for (OrderItemDTO orderItemDTO : orderItemsDTO) {
			orderItems.add(toOrderItem(orderItemDTO));
		}
		return orderItems;
	}
}
