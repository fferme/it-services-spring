package com.ferme.itservices.integration;

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

import static com.ferme.itservices.common.OrderItemConstants.*;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderItemIT {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void createOrderItem_WithValidData_ReturnsCreated() {
		ResponseEntity<OrderItem> sut = restTemplate.postForEntity("/api/orderItems", ORDERITEM_A, OrderItem.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(requireNonNull(sut.getBody()).getId()).isNotNull();
		assertThat(sut.getBody().getOrderItemType()).isEqualTo(ORDERITEM_A.getOrderItemType());
		assertThat(sut.getBody().getDescription()).isEqualTo(ORDERITEM_A.getDescription());
		assertThat(sut.getBody().getPrice()).isEqualTo(ORDERITEM_A.getPrice());
		assertThat(sut.getBody().getOrders()).isEqualTo(ORDERITEM_A.getOrders());
	}

	@Test
	public void createOrderItem_WithInvalidData_ReturnsUnprocessableEntity() {
		ResponseEntity<OrderItem> sut = restTemplate.postForEntity("/api/orderItems", INVALID_ORDERITEM, OrderItem.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void getOrderItem_WithExistingId_ReturnsOrderItem() {
		ResponseEntity<OrderItem> sut = restTemplate.getForEntity("/api/orderItems/1", OrderItem.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).isEqualTo(ORDERITEM_A);
	}

	@Test
	public void listOrderItems_ReturnsAllOrderItems() {
		ResponseEntity<OrderItem[]> sut = restTemplate.getForEntity("/api/orderItems", OrderItem[].class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).hasSize(3);
		assertThat(sut.getBody()[0]).isEqualTo(ORDERITEM_C);
		assertThat(sut.getBody()[1]).isEqualTo(ORDERITEM_B);
		assertThat(sut.getBody()[2]).isEqualTo(ORDERITEM_A);
	}

	@Test
	public void removeOrderItem_ReturnsNoContent() {
		ResponseEntity<Void> sut = restTemplate.exchange("/api/orderItems/" + ORDERITEM_A.getId(), HttpMethod.DELETE, null, Void.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

}