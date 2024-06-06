package com.ferme.itservices.orderItem.unit;

import com.ferme.itservices.api.models.OrderItem;
import com.ferme.itservices.api.repositories.OrderItemRepository;
import com.ferme.itservices.orderItem.utils.OrderItemConstants;
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
public class OrderItemRepositoryTest {
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	private final OrderItemConstants orderItemConstants = OrderItemConstants.getInstance();

	private final OrderItem orderItem = orderItemConstants.ORDERITEM;

	@BeforeEach
	public void cleanup() {
		orderItem.setId(null);
		Mockito.clearAllCaches();
		Mockito.clearInvocations();
	}

	@Test
	public void createOrderItem_WithValidData_ReturnsOrderItem() {
		final OrderItem createdOrderItem = orderItemRepository.save(orderItem);

		final OrderItem sut = testEntityManager.find(OrderItem.class, createdOrderItem.getId());

		assertThat(sut).isNotNull();
		assertThat(sut).isEqualTo(orderItem);
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() {
		final OrderItem createdOrderItem = testEntityManager.persistFlushFind(orderItem);

		final Optional<OrderItem> foundOrderItem = orderItemRepository.findById(createdOrderItem.getId());

		assertThat(foundOrderItem).isNotEmpty();
		assertThat(foundOrderItem.orElse(null)).isEqualTo(createdOrderItem);
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsEmpty() {
		final Optional<OrderItem> foundOrderItem = orderItemRepository.findById(UUID.randomUUID());

		assertThat(foundOrderItem).isEmpty();
	}

	@Sql(scripts = "/scripts/import_orderItems.sql")
	@Test
	public void listOrderItems_WhenOrderItemsExistis_ReturnsAllOrderItems() {
		final List<OrderItem> orderItems = orderItemRepository.findAll();

		assertThat(orderItems).isNotEmpty();
		assertThat(orderItems).hasSize(3);
	}

	@Test
	public void listOrderItems_WhenOrderItemsDoesNotExists_ReturnsNoOrderItems() {
		final List<OrderItem> orderItems = orderItemRepository.findAll();

		assertThat(orderItems).isEmpty();
	}

	@Test
	public void deleteOrderItem_WithExistingId_RemovesOrderItemFromDatabase() {
		final OrderItem createdOrderItem = testEntityManager.persistFlushFind(orderItem);

		orderItemRepository.deleteById(orderItem.getId());
		final OrderItem removedOrderItem = testEntityManager.find(OrderItem.class, createdOrderItem.getId());

		assertThat(removedOrderItem).isNull();
	}

	@Test
	public void deleteOrderItem_WithNonExistingId_DoesNotDeleteAnything() {
		orderItemRepository.deleteById(UUID.randomUUID());

		final OrderItem nonExistingOrderItem = testEntityManager.find(OrderItem.class, UUID.randomUUID());
		assertThat(nonExistingOrderItem).isNull();
	}
}