package com.ferme.itservices.integration;

import com.ferme.itservices.models.Order;
import com.ferme.itservices.models.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.ferme.itservices.common.OrderConstants.*;
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
	public void createOrder_WithValidData_ReturnsCreated() {
		ResponseEntity<Order> sut = restTemplate.postForEntity("/api/orders", ORDER_A, Order.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(requireNonNull(sut.getBody()).getId()).isNotNull();
		assertThat(sut.getBody().getDeviceName()).isEqualTo(ORDER_A.getDeviceName());
		assertThat(sut.getBody().getDeviceSN()).isEqualTo(ORDER_A.getDeviceSN());
		assertThat(sut.getBody().getProblems()).isEqualTo(ORDER_A.getProblems());

		assertThat(sut.getBody().getClient()).isEqualTo(ORDER_A.getClient());

		List<OrderItem> sutOrderItems = sut.getBody().getOrderItems();
		List<OrderItem> expectedOrderItems = ORDER_A.getOrderItems();
		assertEquals(expectedOrderItems, sutOrderItems);
	}

	@Test
	public void createOrder_WithInvalidData_ReturnsUnprocessableEntity() {
		ResponseEntity<Order> sut = restTemplate.postForEntity("/api/orders", INVALID_ORDER, Order.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() {
		ResponseEntity<Order> sut = restTemplate.getForEntity("/api/orders/1", Order.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);

		assertThat(requireNonNull(sut.getBody()).getHeader()).isEqualTo(ORDER_A.getHeader());
		assertThat(requireNonNull(sut.getBody()).getDeviceName()).isEqualTo(ORDER_A.getDeviceName());
		assertThat(requireNonNull(sut.getBody()).getDeviceSN()).isEqualTo(ORDER_A.getDeviceSN());
		assertThat(requireNonNull(sut.getBody()).getProblems()).isEqualTo(ORDER_A.getProblems());
		assertThat(requireNonNull(sut.getBody()).getTotalPrice()).isEqualTo(ORDER_A.getTotalPrice());

		assertThat(sut.getBody().getClient()).isEqualTo(ORDER_A.getClient());
		assertThat(sut.getBody().getOrderItems()).isEqualTo(ORDER_A.getOrderItems());
	}

	@Test
	public void listOrders_ReturnsAllOrders() {
		ResponseEntity<Order[]> sut = restTemplate.getForEntity("/api/orders", Order[].class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).hasSize(3);
		assertThat(sut.getBody()[0]).isEqualTo(ORDER_A);
		assertThat(sut.getBody()[1]).isEqualTo(ORDER_B);
		assertThat(sut.getBody()[2]).isEqualTo(ORDER_C);
	}

	@Test
	public void removeOrder_ReturnsNoContent() {
		ResponseEntity<Void> sut = restTemplate.exchange("/api/orders/1" + ORDER_A.getId(), HttpMethod.DELETE, null, Void.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}