package com.ferme.itservices.common;

import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.models.OrderItem;
import com.ferme.itservices.api.utils.models.DateUtils;
import com.ferme.itservices.api.utils.models.Timestamps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderItemConstants {
    private static final Date date = DateUtils.parseDate("2024-03-19 22:00:00");
    private static final Timestamps timestamps = new Timestamps(date, date);

    public static final OrderItem ORDERITEM = new OrderItem(
        UUID.randomUUID(),
        OrderItemType.MANPOWER,
        "Limpeza completa",
        80.0,
        null,
        timestamps
    );

    public static final List<OrderItem> ORDERITEM_LIST = fillList();

    private static List<OrderItem> fillList() {
        List<OrderItem> orderItems = new ArrayList<>();

        orderItems.add(ORDERITEM);
        orderItems.add(new OrderItem(UUID.randomUUID(), OrderItemType.PART_BUYOUT, "SSD Kingston 240Gb", 120.00, null, timestamps));

        return orderItems;
    }

    public static final OrderItem INVALID_ORDERITEM = new OrderItem(UUID.randomUUID(), null, "", 0.0, null, null);
}