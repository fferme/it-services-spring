package com.ferme.itservices.orderItem;

import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemConstants {
	public static final OrderItem ORDERITEM_A = new OrderItem(1L, OrderItemType.PART_BUYOUT, "SSD de 240Gb", 80.0, null);
	public static final OrderItem ORDERITEM_B = new OrderItem(2L, OrderItemType.MANPOWER, "Limpeza completa", 80.0, null);
	public static final OrderItem ORDERITEM_C = new OrderItem(3L, OrderItemType.CARRIAGE_GOING, "Ilha da Gigóia", 26.00, null);

	public static final OrderItem NEW_ORDERITEM_A = OrderItem.builder()
		.id(1L)
		.orderItemType(OrderItemType.CARRIAGE_BACK)
		.description("New Description")
		.price(1000.0)
		.build();

	public static final OrderItem NEW_ORDERITEM_B = OrderItem.builder()
		.id(2L)
		.orderItemType(OrderItemType.CARRIAGE_GOING)
		.description("New Description 2")
		.price(2000.0)
		.build();

	public static final OrderItem INVALID_ORDERITEM = new OrderItem(null, null, "", -55.0, null);
	public static final OrderItem EMPTY_ORDERITEM = new OrderItem();

	public static final List<OrderItem> ORDER_ITEMS = new ArrayList<>() {
		{
			add(ORDERITEM_A);
			add(ORDERITEM_B);
			add(ORDERITEM_C);
		}
	};

	public static final List<OrderItem> NEW_ORDER_ITEMS = new ArrayList<>() {
		{
			add(NEW_ORDERITEM_A);
			add(NEW_ORDERITEM_B);
		}
	};
}