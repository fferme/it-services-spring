package com.ferme.itservices.integration;

import com.ferme.itservices.models.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.ferme.itservices.common.OrderItemConstants.ORDERITEM_A;
import static com.ferme.itservices.common.OrderItemConstants.ORDERITEM_A_WITH_ID;
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
	public void createOrderItem_ReturnsCreated() {
		ResponseEntity<OrderItem> sut = restTemplate.postForEntity("/api/orderItems", ORDERITEM_A, OrderItem.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(requireNonNull(sut.getBody()).getId()).isNotNull();
		assertThat(sut.getBody().getOrderItemType()).isEqualTo(ORDERITEM_A.getOrderItemType());
		assertThat(sut.getBody().getDescription()).isEqualTo(ORDERITEM_A.getDescription());
		assertThat(sut.getBody().getPrice()).isEqualTo(ORDERITEM_A.getPrice());
		assertThat(sut.getBody().getOrders()).isEqualTo(ORDERITEM_A.getOrders());
	}

	@Test
	public void getOrderItem_ReturnsOrderItem() {
		ResponseEntity<OrderItem> sut = restTemplate.getForEntity("/api/orderItems/1", OrderItem.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).isEqualTo(ORDERITEM_A_WITH_ID);
	}

}