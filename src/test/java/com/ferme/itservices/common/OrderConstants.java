package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.utils.models.DateUtils;
import com.ferme.itservices.api.utils.models.Timestamps;

import java.util.Date;
import java.util.UUID;

public class OrderConstants {
    public static final Order INVALID_ORDER = new Order(UUID.randomUUID(), null, null, null, null, null, null, null);
    private static final Date date = DateUtils.parseDate("2024-03-19 22:00:00");
    private static final Timestamps timestamps = new Timestamps(date, date);
    public static final Order ORDER = new Order(
        UUID.randomUUID(),
        "",
        "Asus Notebook AB9299",
        "9128902183912839021",
        "Erro ao iniciar sistema",
        ClientConstants.CLIENT,
        OrderItemConstants.ORDERITEM_LIST,
        timestamps
    );
}