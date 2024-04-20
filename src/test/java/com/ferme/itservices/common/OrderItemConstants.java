package com.ferme.itservices.common;

import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemConstants {
    public static final OrderItem VALID_ORDERITEM = OrderItem.builder()
       .orderItemType(OrderItemType.MANPOWER)
       .description("Limpeza completa")
       .price(80.0)
       .build();

    public static final OrderItem INVALID_ORDERITEM = OrderItem.builder()
       .orderItemType(null)
       .description("")
       .price(0.0)
       .build();

    public static final List<OrderItem> ORDERITEM_LIST = fillList();
    public static final List<OrderItem> ORDERITEM_INVALID_LIST = fillInvalidList();

    private static List<OrderItem> fillList() {
        List<OrderItem> orderItems = new ArrayList<>();

        orderItems.add(VALID_ORDERITEM);
        orderItems.add(OrderItem.builder()
                          .orderItemType(OrderItemType.PART_BUYOUT)
                          .description("SSD Kingston 240Gb")
                          .price(120.0)
                          //.orders(null)
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