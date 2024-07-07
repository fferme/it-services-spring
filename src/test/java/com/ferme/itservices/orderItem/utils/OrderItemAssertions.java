package com.ferme.itservices.orderItem.utils;

import com.ferme.itservices.api.orderItem.dtos.OrderItemDTO;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemAssertions {
	public static OrderItemAssertions instance;

	private OrderItemAssertions() { }

	public static OrderItemAssertions getInstance() {
		return (instance == null)
			? instance = new OrderItemAssertions()
			: instance;
	}

	public void assertOrderItemProps(OrderItemDTO expectedOrderItemDTO, OrderItemDTO sut) {
		assertThat(sut).isNotNull();
		assertThat(sut.orderItemType()).isEqualTo(expectedOrderItemDTO.orderItemType());
		assertThat(sut.description()).isEqualTo(expectedOrderItemDTO.description());
		assertThat(sut.price()).isEqualTo(expectedOrderItemDTO.price());
	}

	public void assertOrderItemListProps(List<OrderItemDTO> expectedList, List<OrderItemDTO> sut) {
		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSameSizeAs(expectedList);

		for (int i = 0; i < sut.size(); i++) {
			assertOrderItemProps(expectedList.get(i), sut.get(i));
		}
		assertThat(sut).isSortedAccordingTo(Comparator.comparing(OrderItemDTO::description));
	}
}
