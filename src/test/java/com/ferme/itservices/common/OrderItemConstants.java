package com.ferme.itservices.common;

import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemConstants {
	public static final OrderItem ORDERITEM_A = OrderItem.builder()
		.orderItemType(OrderItemType.PART_BUYOUT)
		.description("SSD de 240Gb")
		.price(80.0)
		.build();

	public static final OrderItem ORDERITEM_B = OrderItem.builder()
		.orderItemType(OrderItemType.MANPOWER)
		.description("Limpeza completa")
		.price(80.0)
		.build();

	public static final OrderItem ORDERITEM_C = OrderItem.builder()
		.orderItemType(OrderItemType.CARRIAGE_GOING)
		.description("Ilha da Gigóia")
		.price(26.00)
		.build();

	public static final OrderItem ORDERITEM_A_WITH_ID = OrderItem.builder()
		.id(1L)
		.orderItemType(OrderItemType.PART_BUYOUT)
		.description("SSD de 240Gb")
		.price(80.0)
		.build();

	public static final OrderItem ORDERITEM_B_WITH_ID = OrderItem.builder()
		.id(2L)
		.orderItemType(OrderItemType.MANPOWER)
		.description("Limpeza completa")
		.price(80.0)
		.build();

	public static final OrderItem ORDERITEM_C_WITH_ID = OrderItem.builder()
		.id(3L)
		.orderItemType(OrderItemType.CARRIAGE_GOING)
		.description("Ilha da Gigóia")
		.price(26.00)
		.build();

	public static final OrderItem INVALID_ORDERITEM = OrderItem.builder()
		.orderItemType(null)
		.description("")
		.price(-55.0)
		.build();

	public static final OrderItem EMPTY_ORDERITEM = new OrderItem();

	public static final List<OrderItem> ORDER_ITEMS = new ArrayList<>() {
		{
			add(ORDERITEM_A);
			add(ORDERITEM_B);
			add(ORDERITEM_C);
		}
	};

	public static final List<OrderItem> ORDER_ITEMS_WITH_IDS = new ArrayList<>() {
		{
			add(ORDERITEM_A_WITH_ID);
			add(ORDERITEM_B_WITH_ID);
			add(ORDERITEM_C_WITH_ID);
		}
	};

}