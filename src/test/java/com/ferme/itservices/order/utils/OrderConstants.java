package com.ferme.itservices.order.utils;

import com.ferme.itservices.api.entities.dtos.OrderDTO;
import com.ferme.itservices.api.entities.models.Order;
import com.ferme.itservices.client.utils.ClientConstants;
import com.ferme.itservices.orderItem.utils.OrderItemConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.api.entities.dtos.mappers.OrderMapper.toOrderDTO;
import static com.ferme.itservices.api.entities.dtos.mappers.OrderMapper.toOrderDTOList;

public class OrderConstants {
	public static OrderConstants instance;

	private OrderConstants() { }

	public static OrderConstants getInstance() {
		return (instance == null)
			? instance = new OrderConstants()
			: instance;
	}

	private static final ClientConstants clientConstants = ClientConstants.getInstance();
	private static final OrderItemConstants orderItemConstants = OrderItemConstants.getInstance();

	public final static UUID ORDER_A_UUID = UUID.fromString("e1b64829-6a03-45aa-a12f-211153c3d19c");
	private final static UUID ORDER_B_UUID = UUID.fromString("fdfe1855-8c37-4fce-bdeb-2dcd00ab2ca0");
	private final static UUID ORDER_C_UUID = UUID.fromString("1b4d3287-296c-4a81-9f5b-e234f5aa7aef");

	public final Order ORDER = Order.builder()
		.id(ORDER_A_UUID)
		.deviceName("Asus Notebook AB9299")
		.deviceSN("918390SDA21")
		.issues("Erro ao iniciar sistema")
		.client(clientConstants.CLIENT)
		.orderItems(orderItemConstants.ORDER_ITEMS)
		.totalPrice(1000.0)
		.build();
	public final OrderDTO ORDER_DTO = toOrderDTO(ORDER);

	public final Order NEW_ORDER_CLIENTS_AND_ORDERITEMS = Order.builder()
		.deviceName("New device name")
		.deviceSN("2132141312")
		.issues("New issues")
		.client(clientConstants.NEW_CLIENT)
		.orderItems(orderItemConstants.NEW_ORDER_ITEMS)
		.totalPrice(1400.0)
		.build();
	public final OrderDTO NEW_ORDER_CLIENTS_AND_ORDERITEMS_DTO = toOrderDTO(NEW_ORDER_CLIENTS_AND_ORDERITEMS);

	public final Order NEW_ORDER_EXISTING_CLIENTS_AND_ORDERITEMS = Order.builder()
		.deviceName("New device name")
		.deviceSN("2132141312")
		.issues("New issues")
		.client(clientConstants.CLIENT)
		.orderItems(orderItemConstants.ORDER_ITEMS)
		.totalPrice(1400.0)
		.build();

	public final Order INVALID_ORDER = Order.builder()
		.deviceName(null)
		.deviceSN("")
		.issues("")
		.client(clientConstants.INVALID_CLIENT)
		.orderItems(new ArrayList<>())
		.build();
	public final OrderDTO INVALID_ORDER_DTO = toOrderDTO(INVALID_ORDER);

	public final List<Order> ORDERS = new ArrayList<>() {
		{
			add(
				Order.builder()
					.id(ORDER_A_UUID)
					.deviceName("Asus Notebook AB9299")
					.deviceSN("918390SDA21")
					.issues("Erro ao iniciar sistema")
					.client(clientConstants.CLIENT)
					.orderItems(orderItemConstants.ORDER_ITEMS)
					.totalPrice(1000.0)
					.build()
			);
			add(
				Order.builder()
					.id(ORDER_B_UUID)
					.deviceName("Asus Notebook AB9499")
					.deviceSN("91233301")
					.issues("Erro ao iniciar sistema")
					.client(clientConstants.CLIENTS.get(1))
					.orderItems(orderItemConstants.ORDER_ITEMS)
					.totalPrice(1000.0)
					.build()
			);
			add(
				Order.builder()
					.id(ORDER_C_UUID)
					.deviceName("Asus Notebook AB4599")
					.deviceSN("233314445")
					.issues("Erro ao iniciar sistema")
					.client(clientConstants.CLIENTS.get(2))
					.orderItems(orderItemConstants.ORDER_ITEMS)
					.totalPrice(1200.0)
					.build()
			);
		}
	};
	public final List<OrderDTO> ORDERS_DTO = toOrderDTOList(ORDERS);
}