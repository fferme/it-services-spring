package com.ferme.itservices.orderItem.unit;

import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.orderItem.utils.OrderItemConstants;
import com.ferme.itservices.repositories.OrderItemRepository;
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
		OrderItem orderItemCreated = orderItemRepository.save(orderItem);

		OrderItem sut = testEntityManager.find(OrderItem.class, orderItemCreated.getId());

		assertThat(sut).isNotNull();
		assertThat(sut).isEqualTo(orderItem);
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() {
		OrderItem orderItemGet = testEntityManager.persistFlushFind(orderItem);

		Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItemGet.getId());

		assertThat(orderItemOpt).isNotEmpty();
		assertThat(orderItemOpt.orElse(null)).isEqualTo(orderItemGet);
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsEmpty() {
		Optional<OrderItem> orderItemOpt = orderItemRepository.findById(UUID.randomUUID());

		assertThat(orderItemOpt).isEmpty();
	}

	@Sql(scripts = "/scripts/import_orderItems.sql")
	@Test
	public void listOrderItems_WhenOrderItemsExistis_ReturnsAllOrderItems() {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		assertThat(orderItems).isNotEmpty();
		assertThat(orderItems).hasSize(3);
	}

	@Test
	public void listOrderItems_WhenOrderItemsDoesNotExists_ReturnsNoOrderItems() {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		assertThat(orderItems).isEmpty();
	}

	@Test
	public void deleteOrderItem_WithExistingId_RemovesOrderItemFromDatabase() {
		OrderItem orderItemDel = testEntityManager.persistFlushFind(orderItem);

		orderItemRepository.deleteById(orderItem.getId());
		OrderItem removedOrderItem = testEntityManager.find(OrderItem.class, orderItemDel.getId());

		assertThat(removedOrderItem).isNull();
	}

	@Test
	public void deleteOrderItem_WithNonExistingId_DoesNotDeleteAnything() {
		orderItemRepository.deleteById(UUID.randomUUID());

		OrderItem nonExistingOrderItem = testEntityManager.find(OrderItem.class, UUID.randomUUID());
		assertThat(nonExistingOrderItem).isNull();
	}
}