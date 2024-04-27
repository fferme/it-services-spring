package com.ferme.itservices.orderItem;

import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemConstants {
	public static final OrderItem ORDERITEM_A = new OrderItem(1L, OrderItemType.PART_BUYOUT, "SSD de 240Gb", 80.0, null);
	public static final OrderItem ORDERITEM_B = new OrderItem(2L, OrderItemType.MANPOWER, "Limpeza completa", 80.0, null);
	public static final OrderItem ORDERITEM_C = new OrderItem(3L, OrderItemType.CARRIAGE_GOING, "Ilha da Gigóia", 26.00, null);

	public static final OrderItem INVALID_ORDERITEM = new OrderItem(null, null, "", -55.0, null);
	public static final OrderItem EMPTY_ORDERITEM = new OrderItem();

	public static final List<OrderItem> ORDER_ITEMS = new ArrayList<>() {
		{
			add(ORDERITEM_A);
			add(ORDERITEM_B);
			add(ORDERITEM_C);
		}
	};
}