package com.ferme.itservices.order;

import com.ferme.itservices.models.Order;

import java.util.ArrayList;
import java.util.List;

import static com.ferme.itservices.client.ClientConstants.*;
import static com.ferme.itservices.orderItem.OrderItemConstants.ORDER_ITEMS;

public class OrderConstants {
	public static final Order ORDER_A = Order.builder()
		.deviceName("Asus Notebook AB9299")
		.deviceSN("91839021")
		.problems("Erro ao iniciar sistema")
		.client(FELIPE)
		.orderItems(ORDER_ITEMS)
		.totalPrice(1000.0)
		.build();

	public static final Order ORDER_B = Order.builder()
		.deviceName("Asus Notebook AB9499")
		.deviceSN("91233301")
		.problems("Erro ao iniciar sistema")
		.client(RONALDO)
		.orderItems(ORDER_ITEMS)
		.totalPrice(1000.0)
		.build();

	public static final Order ORDER_C = Order.builder()
		.deviceName("Asus Notebook AB4599")
		.deviceSN("233314445")
		.problems("Erro ao iniciar sistema")
		.client(JOAO)
		.orderItems(ORDER_ITEMS)
		.totalPrice(1000.0)
		.build();

	public static final List<Order> ORDERS = new ArrayList<>() {
		{
			add(ORDER_A);
			add(ORDER_B);
			add(ORDER_C);
		}
	};

	public static final Order INVALID_ORDER = Order.builder()
		.deviceName("")
		.deviceSN("")
		.problems("")
		.client(EMPTY_CLIENT)
		.orderItems(new ArrayList<>())
		.build();

	public static final Order EMPTY_ORDER = new Order();
}