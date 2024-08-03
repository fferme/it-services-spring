package com.ferme.itservices.api.application.utils;

import com.ferme.itservices.api.entities.enums.OrderItemType;
import com.ferme.itservices.api.entities.models.OrderItem;

import java.util.List;

public class Price {
	public static Price instance;

	private Price() { }

	public static Price getInstance() {
		return (instance == null)
			? instance = new Price()
			: instance;
	}

	public Double calculateTotalPrice(final List<OrderItem> orderItems) {
		double totalPrice = 0;
		if (orderItems != null) {
			for (OrderItem orderItem : orderItems) {
				if (orderItem.getOrderItemType().equals(OrderItemType.DISCOUNT)) {
					totalPrice -= orderItem.getPrice();
				} else {
					totalPrice += (orderItem.getQuantity() * orderItem.getPrice());
				}
			}
		}
		return totalPrice;
	}
}
