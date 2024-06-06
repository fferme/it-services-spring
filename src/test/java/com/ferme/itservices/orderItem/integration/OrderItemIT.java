package com.ferme.itservices.orderItem.integration;

import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.orderItem.utils.OrderItemConstants;
import org.assertj.core.api.Assertions;
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

import static com.ferme.itservices.api.dtos.mappers.OrderItemMapper.toOrderItemDTO;
import static com.ferme.itservices.api.dtos.mappers.OrderItemMapper.toOrderItemDTOList;
import static com.ferme.itservices.orderItem.utils.OrderItemConstants.ORDERITEM_A_UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderItemIT {
	@Autowired
	private WebTestClient webTestClient;

	private static final OrderItemConstants orderItemConstants = OrderItemConstants.getInstance();

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void createOrderItem_WithValidData_ReturnsCreated() {
		final OrderItemDTO newOrderItemDTO = toOrderItemDTO(orderItemConstants.NEW_ORDERITEM);

		webTestClient.post().uri("/api/orderItems").bodyValue(newOrderItemDTO)
			.exchange().expectStatus().isCreated()
			.expectBody(OrderItemDTO.class)
			.value(orderItemDTO -> assertThat(orderItemDTO.orderItemType(), is(newOrderItemDTO.orderItemType())))
			.value(orderItemDTO -> assertThat(orderItemDTO.description(), is(newOrderItemDTO.description())))
			.value(orderItemDTO -> assertThat(orderItemDTO.price(), is(newOrderItemDTO.price())));
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void createOrderItem_WithInvalidData_ReturnsUnprocessableEntity() {
		final OrderItemDTO invalidOrderItemDTO = toOrderItemDTO(orderItemConstants.INVALID_ORDERITEM);

		webTestClient.post().uri("/api/orderItems")
			.bodyValue(invalidOrderItemDTO).exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void updateOrderItem_WithValidData_ReturnsUpdatedOrderItem() {
		final OrderItemDTO newOrderItemDTO = toOrderItemDTO(orderItemConstants.ORDERITEM);

		webTestClient.put()
			.uri("/api/orderItems/" + ORDERITEM_A_UUID)
			.bodyValue(newOrderItemDTO).exchange()
			.expectStatus().isEqualTo(HttpStatus.OK);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void getOrderItem_WithExistingId_ReturnsOrderItem() {
		final OrderItemDTO newOrderItemDTO = toOrderItemDTO(orderItemConstants.ORDERITEM);

		webTestClient.get().uri("/api/orderItems/" + ORDERITEM_A_UUID)
			.exchange().expectStatus().isOk()
			.expectBody(OrderItemDTO.class)
			.value(orderItemDTO -> assertThat(orderItemDTO.orderItemType(), is(newOrderItemDTO.orderItemType())))
			.value(orderItemDTO -> assertThat(orderItemDTO.description(), is(newOrderItemDTO.description())))
			.value(orderItemDTO -> assertThat(orderItemDTO.price(), is(newOrderItemDTO.price())));
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void listOrderItems_ReturnsAllOrderItemsSortedByDescription() {
		final List<OrderItemDTO> orderItemsDTO = toOrderItemDTOList(orderItemConstants.ORDER_ITEMS);

		final List<OrderItemDTO> actualOrderItemsDTO = webTestClient.get().uri("/api/orderItems")
			.accept(MediaType.APPLICATION_JSON).exchange()
			.expectStatus().isOk()
			.expectBodyList(OrderItemDTO.class)
			.returnResult().getResponseBody();

		final List<OrderItemDTO> sortedExpectedOrderItemsDTO = orderItemsDTO.stream()
			.sorted(Comparator.comparing(OrderItemDTO::description))
			.toList();

		assert actualOrderItemsDTO != null;
		final List<OrderItemDTO> sortedActualOrderItemsDTO = actualOrderItemsDTO.stream()
			.sorted(Comparator.comparing(OrderItemDTO::description))
			.toList();

		assertEquals(sortedExpectedOrderItemsDTO.size(), sortedActualOrderItemsDTO.size());
		Assertions.assertThat(sortedActualOrderItemsDTO)
			.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
			.containsExactlyElementsOf(sortedExpectedOrderItemsDTO);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void removeOrderItem_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orderItems/" + ORDERITEM_A_UUID)
			.exchange().expectStatus().isNoContent();
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void removeOrderItems_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orderItems")
			.exchange().expectStatus().isNoContent();
	}


}