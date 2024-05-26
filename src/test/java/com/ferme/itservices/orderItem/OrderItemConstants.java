package com.ferme.itservices.orderItem;

import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.models.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.dtos.mappers.OrderItemMapper.toOrderItemDTO;
import static com.ferme.itservices.dtos.mappers.OrderItemMapper.toOrderItemDTOList;

public class OrderItemConstants {

	public static final UUID ORDERITEM_A_UUID = UUID.fromString("0def47bf-ea7e-430a-a1e9-019a1f48d0ec");
	public static final OrderItem ORDERITEM_A = OrderItem.builder()
		.orderItemType(OrderItemType.CARRIAGE_GOING)
		.description("Ilha da Mantiqueira -> Ilha da Gigóia")
		.price(25.0)
		.build();
	public static final OrderItemDTO ORDERITEM_A_DTO = toOrderItemDTO(ORDERITEM_A);

	public static final UUID ORDERITEM_B_UUID = UUID.fromString("88e3c5f7-d0c1-4829-b5e0-61541f9aa2dc");
	public static final OrderItem ORDERITEM_B = OrderItem.builder()
		.orderItemType(OrderItemType.CARRIAGE_BACK)
		.description("Méier -> Cachambi")
		.price(35.0)
		.build();
	public static final OrderItemDTO ORDERITEM_B_DTO = toOrderItemDTO(ORDERITEM_B);

	public static final UUID ORDERITEM_C_UUID = UUID.fromString("817a23cc-8e40-487f-ae27-fed67a113413");
	public static final OrderItem ORDERITEM_C = OrderItem.builder()
		.orderItemType(OrderItemType.MANPOWER)
		.description("Troca de SSD")
		.price(80.0)
		.build();
	public static final OrderItemDTO ORDERITEM_C_DTO = toOrderItemDTO(ORDERITEM_C);

	public static final OrderItem NEW_ORDERITEM = OrderItem.builder()
		.orderItemType(OrderItemType.PART_BUYOUT)
		.description("Memória RAM")
		.price(180.0)
		.build();
	public static final OrderItemDTO NEW_ORDERITEM_DTO = toOrderItemDTO(NEW_ORDERITEM);

	public static final OrderItem ORDERITEM_WITH_ID = OrderItem.builder()
		.orderItemType(OrderItemType.PART_EXCHANGE)
		.description("SSD")
		.price(70.0)
		.build();
	public static final OrderItemDTO ORDERITEM_WITH_ID_DTO = toOrderItemDTO(ORDERITEM_WITH_ID);

	public static final OrderItem EMPTY_ORDERITEM = new OrderItem();
	public static final OrderItemDTO EMPTY_ORDERITEM_DTO = toOrderItemDTO(EMPTY_ORDERITEM);

	public static final OrderItem INVALID_ORDERITEM = OrderItem.builder()
		.orderItemType(null)
		.description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis semper vitae")
		.price(-4.0)
		.build();
	public static final OrderItemDTO INVALID_ORDERITEM_DTO = toOrderItemDTO(INVALID_ORDERITEM);

	public static final List<OrderItem> ORDER_ITEMS = new ArrayList<>() {
		{
			add(ORDERITEM_A);
			add(ORDERITEM_B);
			add(ORDERITEM_C);
		}
	};
	public static final List<OrderItemDTO> ORDER_ITEMS_DTO = toOrderItemDTOList(ORDER_ITEMS);
}