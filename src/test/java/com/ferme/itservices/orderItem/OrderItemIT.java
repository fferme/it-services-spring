package com.ferme.itservices.orderItem;

import com.ferme.itservices.dtos.OrderItemDTO;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderItemIT {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void createOrderItem_WithValidData_ReturnsCreated() {
		webTestClient.post().uri("/api/orderItems").bodyValue(NEW_ORDERITEM_DTO)
			.exchange().expectStatus().isCreated()
			.expectBody()
			.jsonPath("$.orderItemType").isEqualTo(NEW_ORDERITEM_DTO.orderItemType().getValue())
			.jsonPath("$.description").isEqualTo(NEW_ORDERITEM_DTO.description())
			.jsonPath("$.price").isEqualTo(NEW_ORDERITEM_DTO.price());
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
			.uri("/api/orderItems/" + ORDERITEM_A_UUID)
			.bodyValue(NEW_ORDERITEM_DTO).exchange()
			.expectStatus().isEqualTo(HttpStatus.OK);
	}

	@Test
	public void getOrderItem_WithExistingId_ReturnsOrderItem() {
		webTestClient.get().uri("/api/orderItems/" + ORDERITEM_A_UUID)
			.exchange().expectStatus().isOk()
			.expectBody()
			.jsonPath("$.orderItemType").isEqualTo(ORDERITEM_A_DTO.orderItemType().getValue())
			.jsonPath("$.description").isEqualTo(ORDERITEM_A_DTO.description())
			.jsonPath("$.price").isEqualTo(ORDERITEM_A_DTO.price());
	}

	@Test
	public void listOrderItems_ReturnsAllOrderItemsSortedByDescription() {
		List<OrderItemDTO> actualOrderItemsDTO = webTestClient.get().uri("/api/orderItems")
			.accept(MediaType.APPLICATION_JSON).exchange()
			.expectStatus().isOk()
			.expectBodyList(OrderItemDTO.class)
			.returnResult().getResponseBody();

		List<OrderItemDTO> sortedExpectedOrderItemsDTO = ORDER_ITEMS_DTO.stream()
			.sorted(Comparator.comparing(OrderItemDTO::description))
			.toList();

		assert actualOrderItemsDTO != null;
		List<OrderItemDTO> sortedActualOrderItemsDTO = actualOrderItemsDTO.stream()
			.sorted(Comparator.comparing(OrderItemDTO::description))
			.toList();

		assertEquals(sortedExpectedOrderItemsDTO.size(), sortedActualOrderItemsDTO.size());
		assertThat(sortedActualOrderItemsDTO)
			.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
			.containsExactlyElementsOf(sortedExpectedOrderItemsDTO);
	}

	@Test
	public void removeOrderItem_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orderItems/" + ORDERITEM_A_UUID)
			.exchange().expectStatus().isNoContent();
	}

	@Test
	public void removeOrderItems_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orderItems")
			.exchange().expectStatus().isNoContent();
	}


}