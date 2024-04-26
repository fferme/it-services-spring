package com.ferme.itservices.common;

import com.ferme.itservices.models.Order;

import java.util.ArrayList;
import java.util.List;

import static com.ferme.itservices.common.ClientConstants.*;
import static com.ferme.itservices.common.OrderItemConstants.ORDER_ITEMS;

public class OrderConstants {
	public static final Order ORDER_A = new Order(1L, "Asus Notebook AB9299", "91839021", "Erro ao iniciar sistema", FELIPE, ORDER_ITEMS, 1000.0);
	public static final Order ORDER_B = new Order(2L, "Asus Notebook AB9499", "91233301", "Erro ao iniciar sistema", JOAO, ORDER_ITEMS, 1000.0);
	public static final Order ORDER_C = new Order(3L, "Asus Notebook AB4599", "233314445", "Erro ao iniciar sistema", RONALDO, ORDER_ITEMS, 1200.0);

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