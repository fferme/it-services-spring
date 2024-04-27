package com.ferme.itservices.order;

import com.ferme.itservices.models.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.ferme.itservices.order.OrderConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
	private WebTestClient webTestClient;

	@Test
	public void createOrder_WithValidData_ReturnsCreated() {
		webTestClient.post().uri("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(ORDER_A)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(Order.class)
			.value(order -> assertThat(order.getId(), notNullValue()))
			.value(order -> assertThat(order.getDeviceName(), is(ORDER_A.getDeviceName())))
			.value(order -> assertThat(order.getDeviceSN(), is(ORDER_A.getDeviceSN())))
			.value(order -> assertThat(order.getProblems(), is(ORDER_A.getProblems())))
			.value(order -> assertThat(order.getClient(), is(ORDER_A.getClient())))
			.value(order -> assertThat(order.getOrderItems(), is(ORDER_A.getOrderItems())));
	}

	@Test
	public void createOrder_WithInvalidData_ReturnsUnprocessableEntity() {
		webTestClient.post().uri("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(INVALID_ORDER)
			.exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() {
		webTestClient.get().uri("/api/orders/" + ORDER_A.getId())
			.exchange()
			.expectStatus().isOk()
			.expectBody(Order.class)
			.value(order -> assertThat(order.getHeader(), is(ORDER_A.getHeader())))
			.value(order -> assertThat(order.getDeviceName(), is(ORDER_A.getDeviceName())))
			.value(order -> assertThat(order.getDeviceSN(), is(ORDER_A.getDeviceSN())))
			.value(order -> assertThat(order.getProblems(), is(ORDER_A.getProblems())))
			.value(order -> assertThat(order.getTotalPrice(), is(ORDER_A.getTotalPrice())))
			.value(order -> assertThat(order.getClient(), is(ORDER_A.getClient())))
			.value(order -> assertThat(order.getOrderItems(), is(ORDER_A.getOrderItems())));
	}

	@Test
	public void listOrders_ReturnsAllOrders() {
		webTestClient.get().uri("/api/orders")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Order.class)
			.hasSize(3)
			.contains(ORDER_A, ORDER_B, ORDER_C);
	}

	@Test
	public void removeOrder_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orders/" + ORDER_A.getId())
			.exchange().expectStatus().isNoContent();
	}
}