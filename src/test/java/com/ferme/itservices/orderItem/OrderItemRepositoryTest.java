package com.ferme.itservices.orderItem;

import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.OrderItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ferme.itservices.orderItem.OrderItemConstants.ORDERITEM_A;
import static org.assertj.core.api.Assertions.assertThat;

;

@DataJpaTest
public class OrderItemRepositoryTest {
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@AfterEach
	public void nullifyId() {
		ORDERITEM_A.setId(null);
	}

	@Test
	public void createOrderItem_WithValidData_ReturnsOrderItem() {
		OrderItem orderItem = orderItemRepository.save(ORDERITEM_A);

		OrderItem sut = testEntityManager.find(OrderItem.class, orderItem.getId());

		assertThat(sut).isNotNull();
		assertThat(sut).isEqualTo(ORDERITEM_A);
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() {
		OrderItem orderItem = testEntityManager.persistFlushFind(ORDERITEM_A);

		Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItem.getId());

		assertThat(orderItemOpt).isNotEmpty();
		assertThat(orderItemOpt.orElse(null)).isEqualTo(orderItem);
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsEmpty() {
		Optional<OrderItem> orderItemOpt = orderItemRepository.findById(UUID.randomUUID());

		assertThat(orderItemOpt).isEmpty();
	}

	@Sql(scripts = "/scripts/import_orderItems.sql")
	@Test
	public void listOrderItems_WhenOrderItemsExistis_ReturnsAllOrderItems() throws Exception {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		assertThat(orderItems).isNotEmpty();
		assertThat(orderItems).hasSize(3);
	}

	@Test
	public void listOrderItems_WhenOrderItemsDoesNotExists_ReturnsNoOrderItems() throws Exception {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		assertThat(orderItems).isEmpty();
	}

	@Test
	public void deleteOrderItem_WithExistingId_RemovesOrderItemFromDatabase() {
		OrderItem orderItem = testEntityManager.persistFlushFind(ORDERITEM_A);

		orderItemRepository.deleteById(orderItem.getId());
		OrderItem removedOrderItem = testEntityManager.find(OrderItem.class, orderItem.getId());

		assertThat(removedOrderItem).isNull();
	}

	@Test
	public void deleteOrderItem_WithNonExistingId_DoesNotDeleteAnything() {
		orderItemRepository.deleteById(UUID.randomUUID());

		OrderItem nonExistingOrderItem = testEntityManager.find(OrderItem.class, UUID.randomUUID());
		assertThat(nonExistingOrderItem).isNull();
	}
}