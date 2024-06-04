package com.ferme.itservices.order.unit;

import com.ferme.itservices.models.Order;
import com.ferme.itservices.order.utils.OrderConstants;
import com.ferme.itservices.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class OrderRepositoryTest {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	final Order order = orderConstants.ORDER;

	private void nullifyIds() {
		order.setId(null);
		order.getClient().setId(null);
		order.getOrderItems().forEach(orderItem -> orderItem.setId(null));
	}

	@BeforeEach
	public void cleanup() {
		nullifyIds();
		Mockito.clearAllCaches();
		Mockito.clearInvocations();
	}

	private static final OrderConstants orderConstants = OrderConstants.getInstance();

	@Test
	public void createOrder_WithValidData_ReturnsOrder() {
		Order savedOrder = orderRepository.save(order);

		Order sut = testEntityManager.find(Order.class, savedOrder.getId());

		assertThat(sut).isNotNull();
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() {
		Order savedOrder = testEntityManager.persistFlushFind(order);

		Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

		assertThat(foundOrder).isNotEmpty();
		assertThat(foundOrder.orElse(null)).isEqualTo(savedOrder);
	}

	@Test
	public void getOrder_ByUnexistingId_ReturnsEmpty() {
		Optional<Order> foundOrder = orderRepository.findById(UUID.randomUUID());

		assertThat(foundOrder).isEmpty();
	}

	@Sql(
		scripts = {
			"/scripts/import_clients.sql",
			"/scripts/import_orderItems.sql",
			"/scripts/import_orders.sql"
		})
	@Test
	public void listOrders_WhenOrdersExists_ReturnsAllOrders() {
		List<Order> orders = orderRepository.findAll();

		assertThat(orders).isNotEmpty();
		assertThat(orders).hasSize(3);
	}

	@Test
	public void listOrders_WhenOrdersDoesNotExists_ReturnsEmptyList() {
		List<Order> orders = orderRepository.findAll();

		assertThat(orders).isEmpty();
	}

	@Test
	public void deleteOrder_WithExistingId_RemovesOrderFromDatabase() {
		Order savedOrder = testEntityManager.persistFlushFind(order);

		orderRepository.deleteById(savedOrder.getId());
		Order removedOrder = testEntityManager.find(Order.class, savedOrder.getId());

		assertThat(removedOrder).isNull();
	}

	@Test
	public void deleteOrder_WithNonExistingId_DoesNotDeleteAnything() {
		orderRepository.deleteById(UUID.randomUUID());

		Order nonExistingOrder = testEntityManager.find(Order.class, UUID.randomUUID());
		assertThat(nonExistingOrder).isNull();
	}
}