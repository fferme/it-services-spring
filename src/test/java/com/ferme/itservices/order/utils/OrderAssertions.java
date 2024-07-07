package com.ferme.itservices.order.utils;

import com.ferme.itservices.api.order.dtos.OrderDTO;
import com.ferme.itservices.client.utils.ClientAssertions;
import com.ferme.itservices.orderItem.utils.OrderItemAssertions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAssertions {
	public static OrderAssertions instance;

	private OrderAssertions() { }

	public static OrderAssertions getInstance() {
		return (instance == null)
			? instance = new OrderAssertions()
			: instance;
	}

	private static final ClientAssertions clientAssertions = ClientAssertions.getInstance();
	private static final OrderItemAssertions orderItemAssertions = OrderItemAssertions.getInstance();

	public void assertOrderProps(OrderDTO expectedOrderDTO, OrderDTO sut) {
		assertThat(sut).isNotNull();
		assertThat(sut.header()).isEqualTo(expectedOrderDTO.header());
		assertThat(sut.deviceName()).isEqualTo(expectedOrderDTO.deviceName());
		assertThat(sut.deviceSN()).isEqualTo(expectedOrderDTO.deviceSN());
		assertThat(sut.deviceSN()).isEqualTo(expectedOrderDTO.deviceSN());
		assertThat(sut.totalPrice()).isEqualTo(expectedOrderDTO.totalPrice());

		clientAssertions.assertClientProps(sut.clientDTO(), expectedOrderDTO.clientDTO());
		orderItemAssertions.assertOrderItemListProps(sut.orderItemsDTO(), expectedOrderDTO.orderItemsDTO());
	}

	public void assertOrderListProps(List<OrderDTO> expectedList, List<OrderDTO> sut) {
		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSameSizeAs(expectedList);

		for (int i = 0; i < sut.size(); i++) {
			assertOrderProps(expectedList.get(i), sut.get(i));
		}
	}
}
