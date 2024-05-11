package com.ferme.itservices.order;

import com.ferme.itservices.models.Order;

import java.util.ArrayList;
import java.util.List;

import static com.ferme.itservices.client.ClientConstants.*;
import static com.ferme.itservices.orderItem.OrderItemConstants.NEW_ORDER_ITEMS;
import static com.ferme.itservices.orderItem.OrderItemConstants.ORDER_ITEMS;

public class OrderConstants {
	public static final Order ORDER_A = new Order(1L, "Asus Notebook AB9299", "91839021", "Erro ao iniciar sistema", FELIPE, ORDER_ITEMS, 1000.0);
	public static final Order ORDER_B = new Order(2L, "Asus Notebook AB9499", "91233301", "Erro ao iniciar sistema", JOAO, ORDER_ITEMS, 1000.0);
	public static final Order ORDER_C = new Order(3L, "Asus Notebook AB4599", "233314445", "Erro ao iniciar sistema", RONALDO, ORDER_ITEMS, 1200.0);

	public static final Order NEW_ORDER = Order.builder()
		.deviceName("New device name")
		.deviceSN("2132141312")
		.problems("New problems")
		.client(NEW_CLIENT)
		.orderItems(NEW_ORDER_ITEMS)
		.build();

	public static final Order INVALID_ORDER = new Order(null, "", "", "", EMPTY_CLIENT, new ArrayList<>(), 0.0);
	public static final Order EMPTY_ORDER = new Order();

	public static final List<Order> ORDERS = new ArrayList<>() {
		{
			add(ORDER_A);
			add(ORDER_B);
			add(ORDER_C);
		}
	};
}