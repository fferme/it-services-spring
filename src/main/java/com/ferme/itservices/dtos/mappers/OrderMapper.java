package com.ferme.itservices.dtos.mappers;

import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.dtos.OrderDTO;
import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.models.Order;
import com.ferme.itservices.models.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ferme.itservices.dtos.mappers.ClientMapper.toClient;

public abstract class OrderMapper {
	public static Order toOrder(OrderDTO orderDTO) {
		Order order = new Order();
		order.setDeviceName(orderDTO.deviceName());
		order.setDeviceSN(orderDTO.deviceSN());
		order.setIssues(orderDTO.issues());

		Client client = toClient(orderDTO.clientDTO());
		List<OrderItem> orderItems = orderDTO.orderItemsDTO().stream()
			.map(orderItemDTO -> {
				OrderItem orderItem = new OrderItem();
				if (orderItemDTO.id() != null) { orderItem.setId(orderItemDTO.id()); }

				orderItem.setOrderItemType(orderItemDTO.orderItemType());
				orderItem.setDescription(orderItemDTO.description());
				orderItem.setPrice(orderItemDTO.price());

				return orderItem;
			}).collect(Collectors.toList());

		order.setClient(client);
		order.setOrderItems(orderItems);
		order.setTotalPrice(orderDTO.totalPrice());

		return order;
	}

	public static OrderDTO toOrderDTO(Order order) {
		if (order == null) { return null; }

		Client orderClient = order.getClient();
		ClientDTO clientDTO = ClientDTO.builder()
			.id(orderClient.getId())
			.name(orderClient.getName())
			.phoneNumber(orderClient.getPhoneNumber())
			.neighborhood(orderClient.getNeighborhood())
			.address(orderClient.getAddress())
			.reference(orderClient.getReference())
			.build();

		List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

		if (order.getOrderItems() != null) {
			orderItemDTOs = order.getOrderItems()
				.stream()
				.map(orderItem -> new OrderItemDTO(
					orderItem.getId(),
					orderItem.getOrderItemType(),
					orderItem.getDescription(),
					orderItem.getPrice(),
					null
				))
				.toList();
		}

		return new OrderDTO(
			order.getId(),
			order.getHeader(),
			order.getDeviceName(),
			order.getDeviceSN(),
			order.getIssues(),
			clientDTO,
			orderItemDTOs,
			order.getTotalPrice()
		);
	}


	public static List<OrderDTO> toOrderDTOList(List<Order> orders) {
		if (orders == null) { return null; }

		return orders.stream()
			.map(OrderMapper::toOrderDTO)
			.collect(Collectors.toList());
	}

	public static List<Order> toOrderList(List<OrderDTO> orderDTOs) {
		return orderDTOs.stream()
			.map(OrderMapper::toOrder)
			.collect(Collectors.toList());
	}
}