package com.ferme.itservices.integration;

import com.ferme.itservices.models.Order;
import com.ferme.itservices.models.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.ferme.itservices.common.OrderConstants.ORDER_A;
import static com.ferme.itservices.common.OrderConstants.ORDER_A_WITH_ID;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(
	scripts = {
		"/scripts/import_clients.sql",
		"/scripts/import_orderItems.sql",
		"/scripts/import_orders.sql"
	}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderIT {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void createOrder_ReturnsCreated() {
		ResponseEntity<Order> sut = restTemplate.postForEntity("/api/orders", ORDER_A, Order.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(requireNonNull(sut.getBody()).getId()).isNotNull();
		assertThat(sut.getBody().getDeviceName()).isEqualTo(ORDER_A.getDeviceName());
		assertThat(sut.getBody().getDeviceSN()).isEqualTo(ORDER_A.getDeviceSN());
		assertThat(sut.getBody().getProblems()).isEqualTo(ORDER_A.getProblems());

		assertThat(sut.getBody().getClient().getName()).isEqualTo(ORDER_A.getClient().getName());
		assertThat(sut.getBody().getClient().getPhoneNumber()).isEqualTo(ORDER_A.getClient().getPhoneNumber());
		assertThat(sut.getBody().getClient().getNeighborhood()).isEqualTo(ORDER_A.getClient().getNeighborhood());
		assertThat(sut.getBody().getClient().getAddress()).isEqualTo(ORDER_A.getClient().getAddress());
		assertThat(sut.getBody().getClient().getReference()).isEqualTo(ORDER_A.getClient().getReference());

		List<OrderItem> sutOrderItems = sut.getBody().getOrderItems();
		List<OrderItem> expectedOrderItems = ORDER_A.getOrderItems();
		for (int i = 0; i < expectedOrderItems.size(); i++) {
			OrderItem expectedOrderItem = expectedOrderItems.get(i);
			OrderItem sutOrderItem = sutOrderItems.get(i);

			assertEquals(expectedOrderItem.getOrderItemType(), sutOrderItem.getOrderItemType());
			assertEquals(expectedOrderItem.getDescription(), sutOrderItem.getDescription());
			assertEquals(expectedOrderItem.getPrice(), sutOrderItem.getPrice());
		}
	}

	@Test
	public void getOrder_ReturnsOrder() {
		ResponseEntity<Order> sut = restTemplate.getForEntity("/api/orders/1", Order.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(requireNonNull(sut.getBody()).getHeader()).isEqualTo(ORDER_A_WITH_ID.getHeader());
		assertThat(requireNonNull(sut.getBody()).getDeviceName()).isEqualTo(ORDER_A_WITH_ID.getDeviceName());
		assertThat(requireNonNull(sut.getBody()).getDeviceSN()).isEqualTo(ORDER_A_WITH_ID.getDeviceSN());
		assertThat(requireNonNull(sut.getBody()).getProblems()).isEqualTo(ORDER_A_WITH_ID.getProblems());
		assertThat(requireNonNull(sut.getBody()).getTotalPrice()).isEqualTo(ORDER_A_WITH_ID.getTotalPrice());

		assertThat(sut.getBody().getClient()).isEqualTo(ORDER_A_WITH_ID.getClient());
		assertThat(sut.getBody().getOrderItems()).isEqualTo(ORDER_A_WITH_ID.getOrderItems());
	}

}