package com.ferme.itservices.orderItem.utils;

import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.models.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class OrderItemConstants {
	public static OrderItemConstants instance;

	private OrderItemConstants() { }

	public static OrderItemConstants getInstance() {
		return (instance == null)
			? instance = new OrderItemConstants()
			: instance;
	}

	public static final UUID ORDERITEM_A_UUID = UUID.fromString("0def47bf-ea7e-430a-a1e9-019a1f48d0ec");
	private final UUID ORDERITEM_B_UUID = UUID.fromString("78e4961d-ee65-4a7b-b0c0-280b72654b7c");
	private final UUID ORDERITEM_C_UUID = UUID.fromString("817a23cc-8e40-487f-ae27-fed67a113413");

	public final OrderItem ORDERITEM = new OrderItem(
		ORDERITEM_A_UUID,
		OrderItemType.CARRIAGE_GOING,
		"Ilha da Mantiqueira -> Ilha da Gigóia",
		25.0,
		null
	);

	public final OrderItem NEW_ORDERITEM = OrderItem.builder()
		.orderItemType(OrderItemType.PART_BUYOUT)
		.description("Memória RAM")
		.price(180.0)
		.build();

	public final OrderItem EMPTY_ORDERITEM = new OrderItem();
	public final OrderItem INVALID_ORDERITEM = OrderItem.builder()
		.orderItemType(null)
		.description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis semper vitae")
		.price(-4.0)
		.build();

	public final List<OrderItem> ORDER_ITEMS = new ArrayList<>() {
		{
			add(new OrderItem(
				ORDERITEM_A_UUID,
				OrderItemType.CARRIAGE_GOING,
				"Ilha da Mantiqueira -> Ilha da Gigóia",
				25.0,
				null
			));
			add(new OrderItem(
				ORDERITEM_B_UUID,
				OrderItemType.CARRIAGE_BACK,
				"Méier -> Cachambi",
				35.0,
				null
			));
			add(new OrderItem(
				ORDERITEM_C_UUID,
				OrderItemType.MANPOWER,
				"Troca de SSD",
				80.0,
				null
			));
		}
	};

	public final List<OrderItem> NEW_ORDER_ITEMS = new ArrayList<>() {
		{
			add(OrderItem.builder()
				    .orderItemType(OrderItemType.MANPOWER)
				    .description("Limpeza Completa")
				    .price(100.0)
				    .build()
			);
			add(OrderItem.builder()
				    .orderItemType(OrderItemType.PART_BUYOUT)
				    .description("Memória RAM")
				    .price(90.0)
				    .build()
			);
			add(OrderItem.builder()
				    .orderItemType(OrderItemType.MANPOWER)
				    .description("Troca de Cartão de memória")
				    .price(80.0)
				    .build()
			);
		}
	};
}