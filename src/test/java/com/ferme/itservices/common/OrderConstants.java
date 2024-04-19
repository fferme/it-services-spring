package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.utils.models.Timestamps;

import static com.ferme.itservices.common.ClientConstants.INVALID_CLIENT;
import static com.ferme.itservices.common.ClientConstants.VALID_CLIENT;
import static com.ferme.itservices.common.OrderItemConstants.ORDERITEM_INVALID_LIST;
import static com.ferme.itservices.common.OrderItemConstants.ORDERITEM_LIST;

public class OrderConstants {
    public static final Order VALID_ORDER = Order.builder()
       .header("ORÇAMENTOS DE SERVIÇOS DE TERCEIROS - PESSOA FÍSICA, DESCRIÇÃO DE SERVIÇO(S) PRESTADO(S)")
       .deviceName("Asus Notebook AB9299")
       .deviceSN("9128902183912839021")
       .problems("Erro ao iniciar sistema")
       .client(VALID_CLIENT)
       .orderItems(ORDERITEM_LIST)
       .timestamps(new Timestamps())
       .build();

    public static final Order INVALID_ORDER = Order.builder()
       .header(null)
       .deviceName("")
       .deviceSN("")
       .problems("")
       .client(INVALID_CLIENT)
       .orderItems(ORDERITEM_INVALID_LIST)
       .timestamps(null)
       .build();
}