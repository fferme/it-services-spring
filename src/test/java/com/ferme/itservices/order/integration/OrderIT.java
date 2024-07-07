package com.ferme.itservices.order.integration;

import com.ferme.itservices.api.client.dtos.ClientDTO;
import com.ferme.itservices.api.order.dtos.OrderDTO;
import com.ferme.itservices.api.orderItem.dtos.OrderItemDTO;
import com.ferme.itservices.order.utils.OrderAssertions;
import com.ferme.itservices.order.utils.OrderConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.ferme.itservices.api.order.dtos.mappers.OrderMapper.toOrderDTO;
import static com.ferme.itservices.api.order.dtos.mappers.OrderMapper.toOrderDTOList;
import static com.ferme.itservices.order.utils.OrderConstants.ORDER_A_UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderIT {
	@Autowired
	private WebTestClient webTestClient;

	private final OrderConstants orderConstants = OrderConstants.getInstance();
	private final OrderAssertions orderAssertions = OrderAssertions.getInstance();

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void createOrder_WithNewClientAndOrderItems_ReturnsCreated() {

		final OrderDTO newOrderDTO = toOrderDTO(orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS);

		webTestClient.post().uri("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(newOrderDTO)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(OrderDTO.class)
			.value(orderDTO -> assertThat(orderDTO.id(), notNullValue()))
			.value(orderDTO -> assertThat(orderDTO.header(), is(newOrderDTO.header())))
			.value(orderDTO -> assertThat(orderDTO.deviceName(), is(newOrderDTO.deviceName())))
			.value(orderDTO -> assertThat(orderDTO.deviceSN(), is(newOrderDTO.deviceSN())))
			.value(orderDTO -> assertThat(orderDTO.issues(), is(newOrderDTO.issues())))
			.value(orderDTO -> {
				final ClientDTO foundClientDTO = orderDTO.clientDTO();
				final ClientDTO clientDTO = newOrderDTO.clientDTO();

				assertThat(foundClientDTO.name(), is(clientDTO.name()));
				assertThat(foundClientDTO.phoneNumber(), is(clientDTO.phoneNumber()));
				assertThat(foundClientDTO.neighborhood(), is(clientDTO.neighborhood()));
				assertThat(foundClientDTO.address(), is(clientDTO.address()));
				assertThat(foundClientDTO.reference(), is(clientDTO.reference()));

				final List<OrderItemDTO> foundOrderItemsDTO = orderDTO.orderItemsDTO();
				final List<OrderItemDTO> orderItemsDTO = newOrderDTO.orderItemsDTO();

				assertThat(foundOrderItemsDTO.size(), is(orderItemsDTO.size()));
				Assertions.assertThat(foundOrderItemsDTO)
					.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
					.containsExactlyElementsOf(orderItemsDTO);
			});
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql", "/scripts/import_orderItems.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void createOrder_WithExistingClientAndOrderItems_ReturnsCreated() {
		final OrderDTO newOrderDTO = toOrderDTO(orderConstants.NEW_ORDER_EXISTING_CLIENTS_AND_ORDERITEMS);

		webTestClient.post().uri("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(newOrderDTO)
			.exchange()
			.expectStatus().isCreated()
			.expectBody(OrderDTO.class)
			.value(orderDTO -> assertThat(orderDTO.id(), notNullValue()))
			.value(orderDTO -> assertThat(orderDTO.header(), is(newOrderDTO.header())))
			.value(orderDTO -> assertThat(orderDTO.deviceName(), is(newOrderDTO.deviceName())))
			.value(orderDTO -> assertThat(orderDTO.deviceSN(), is(newOrderDTO.deviceSN())))
			.value(orderDTO -> assertThat(orderDTO.issues(), is(newOrderDTO.issues())))
			.value(orderDTO -> {
				final ClientDTO foundClientDTO = orderDTO.clientDTO();
				final ClientDTO clientDTO = newOrderDTO.clientDTO();

				assertThat(foundClientDTO.name(), is(clientDTO.name()));
				assertThat(foundClientDTO.phoneNumber(), is(clientDTO.phoneNumber()));
				assertThat(foundClientDTO.neighborhood(), is(clientDTO.neighborhood()));
				assertThat(foundClientDTO.address(), is(clientDTO.address()));
				assertThat(foundClientDTO.reference(), is(clientDTO.reference()));

				final List<OrderItemDTO> foundOrderItemsDTO = orderDTO.orderItemsDTO();
				final List<OrderItemDTO> orderItemsDTO = newOrderDTO.orderItemsDTO();

				assertThat(foundOrderItemsDTO.size(), is(orderItemsDTO.size()));
				Assertions.assertThat(foundOrderItemsDTO)
					.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
					.containsExactlyElementsOf(orderItemsDTO);
			});
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void createOrder_WithInvalidData_ReturnsUnprocessableEntity() {
		final OrderDTO invalidOrderDTO = toOrderDTO(orderConstants.INVALID_ORDER);

		webTestClient.post().uri("/api/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(invalidOrderDTO)
			.exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(
		scripts = {"/scripts/import_clients.sql", "/scripts/import_orderItems.sql", "/scripts/import_orders.sql"},
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
	)
	public void updateOrder_WithValidData_ReturnsUpdatedOrder() {
		final OrderDTO newOrderDTO = toOrderDTO(orderConstants.NEW_ORDER_CLIENTS_AND_ORDERITEMS);

		final EntityExchangeResult<OrderDTO> result = webTestClient.put()
			.uri("/api/orders/" + ORDER_A_UUID)
			.bodyValue(newOrderDTO)
			.exchange()
			.expectStatus().isOk()
			.expectBody(OrderDTO.class)
			.returnResult();

		orderAssertions.assertOrderProps(newOrderDTO, result.getResponseBody());
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(
		scripts = {"/scripts/import_clients.sql", "/scripts/import_orderItems.sql", "/scripts/import_orders.sql"},
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
	)
	public void getOrder_ByExistingId_ReturnsOrder() {
		final OrderDTO orderDTO = toOrderDTO(orderConstants.ORDER);

		final EntityExchangeResult<OrderDTO> result = webTestClient.get().uri("/api/orders/" + orderDTO.id())
			.exchange()
			.expectStatus().isOk()
			.expectBody(OrderDTO.class)
			.returnResult();

		orderAssertions.assertOrderProps(orderDTO, result.getResponseBody());
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(
		scripts = {"/scripts/import_clients.sql", "/scripts/import_orderItems.sql", "/scripts/import_orders.sql"},
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
	)
	public void listOrders_ReturnsAllOrders() {
		final List<OrderDTO> expectedOrderDTOList = toOrderDTOList(orderConstants.ORDERS);

		final List<OrderDTO> actualOrders = webTestClient.get().uri("/api/orders")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(OrderDTO.class)
			.returnResult().getResponseBody();
		assert actualOrders != null;

		Assertions.assertThat(actualOrders).containsExactlyInAnyOrderElementsOf(expectedOrderDTOList);
	}

	@Test
	public void removeOrder_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orders/" + ORDER_A_UUID)
			.exchange().expectStatus().isNoContent();
	}

	@Test
	public void removeOrders_ReturnsNoContent() {
		webTestClient.delete().uri("/api/orders")
			.exchange().expectStatus().isNoContent();
	}
}