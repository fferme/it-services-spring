package com.ferme.itservices.common;

import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemConstants {
	public static final OrderItem VALID_ORDERITEM = OrderItem.builder()
		.orderItemType(OrderItemType.MANPOWER)
		.description("Limpeza completa")
		.price(80.0)
		.build();

	public static final OrderItem INVALID_ORDERITEM = OrderItem.builder()
		.description("")
		.price(0.0)
		.build();

	public static final OrderItem EMPTY_ORDERITEM = new OrderItem();

	public static final List<OrderItem> ORDERITEM_LIST = fillList();
	public static final List<OrderItem> ORDERITEM_INVALID_LIST = fillInvalidList();

	private static List<OrderItem> fillList() {
		List<OrderItem> orderItems = new ArrayList<>();

		orderItems.add(VALID_ORDERITEM);
		orderItems.add(OrderItem.builder()
			               .orderItemType(OrderItemType.PART_BUYOUT)
			               .description("SSD Kingston 240Gb")
			               .price(120.0)
			               .build());

		return orderItems;
	}

	private static List<OrderItem> fillInvalidList() {
		List<OrderItem> orderItems = new ArrayList<>();

		orderItems.add(INVALID_ORDERITEM);
		orderItems.add(INVALID_ORDERITEM);

		return orderItems;
	}
}