package com.ferme.itservices.orderItem;

import com.ferme.itservices.models.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Comparator;
import java.util.List;

import static com.ferme.itservices.orderItem.OrderItemConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderItemIT {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void createOrderItem_WithValidData_ReturnsCreated() {
		webTestClient.post().uri("/api/orderItems").bodyValue(ORDERITEM_A)
			.exchange().expectStatus().isCreated()
			.expectBody(OrderItem.class).isEqualTo(ORDERITEM_A);
	}

	@Test
	public void createOrderItem_WithInvalidData_ReturnsUnprocessableEntity() {
		webTestClient.post().uri("/api/orderItems")
			.bodyValue(INVALID_ORDERITEM).exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void updateOrderItem_WithValidData_ReturnsUpdatedOrderItem() {
		webTestClient.put()
			.uri("/api/orderItems/1")
			.bodyValue(NEW_ORDERITEM_A).exchange()
			.expectStatus().isEqualTo(HttpStatus.OK);
	}

	@Test
	public void getOrderItem_WithExistingId_ReturnsOrderItem() {
		webTestClient.get().uri("/api/orderItems/" + ORDERITEM_A.getId())
			.exchange().expectStatus().isOk()
			.expectBody(OrderItem.class).isEqualTo(ORDERITEM_A);
	}

	@Test
	public void listOrderItems_ReturnsAllOrderItemsSortedByDescription() {
		List<OrderItem> actualOrderItems = webTestClient.get().uri("/api/orderItems")
			.accept(MediaType.APPLICATION_JSON).exchange()
			.expectStatus().isOk()
			.expectBodyList(OrderItem.class)
			.returnResult().getResponseBody();

		List<OrderItem> sortedExpectedOrderItems = ORDER_ITEMS.stream()
			.sorted(Comparator.comparing(OrderItem::getDescription))
			.toList();

		assert actualOrderItems != null;
		List<OrderItem> sortedActualOrderItems = actualOrderItems.stream()
			.sorted(Comparator.comparing(OrderItem::getDescription))
			.toList();

		assertThat(sortedActualOrderItems).containsExactlyElementsOf(sortedExpectedOrderItems);
	}

	@Test
	public void removeOrderItem_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orderItems/" + ORDERITEM_A.getId())
			.exchange().expectStatus().isNoContent();
	}

	@Test
	public void removeOrderItems_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orderItems")
			.exchange().expectStatus().isNoContent();
	}


}