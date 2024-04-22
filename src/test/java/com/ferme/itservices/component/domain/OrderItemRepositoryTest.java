package com.ferme.itservices.component.domain;

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

import static com.ferme.itservices.orderItem.OrderItemConstants.INVALID_ORDERITEM;
import static com.ferme.itservices.orderItem.OrderItemConstants.ORDERITEM_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
		assertThat(sut.getOrderItemType()).isEqualTo(ORDERITEM_A.getOrderItemType());
		assertThat(sut.getDescription()).isEqualTo(ORDERITEM_A.getDescription());
		assertThat(sut.getPrice()).isEqualTo(ORDERITEM_A.getPrice());
	}

	@Test
	public void createOrderItem_WithInvalidData_ThrowsException() {
		OrderItem emptyOrderItem = new OrderItem();
		assertThatThrownBy(() -> orderItemRepository.save(emptyOrderItem)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> orderItemRepository.save(INVALID_ORDERITEM)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void createOrderItem_WithExistingDescription_ThrowsException() {
		OrderItem orderItem = testEntityManager.persistFlushFind(ORDERITEM_A);
		testEntityManager.detach(orderItem);
		orderItem.setId(null);

		assertThatThrownBy(() -> orderItemRepository.save(orderItem)).isInstanceOf(RuntimeException.class);
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
		Optional<OrderItem> orderItemOpt = orderItemRepository.findById(1L);

		assertThat(orderItemOpt).isEmpty();
	}

	@Sql(scripts = "/resources/scripts/import_orderItems.sql")
	@Test
	public void listOrderItems_ReturnsOrderItems() throws Exception {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		assertThat(orderItems).isNotEmpty();
		assertThat(orderItems).hasSize(3);
	}

	@Test
	public void listOrderItems_ReturnsNoOrderItems() throws Exception {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		assertThat(orderItems).isEmpty();
	}

	@Test
	public void removeOrderItem_WithExistingId_RemovesOrderItemFromDatabase() {
		OrderItem orderItem = testEntityManager.persistFlushFind(ORDERITEM_A);

		orderItemRepository.deleteById(orderItem.getId());
		OrderItem removedOrderItem = testEntityManager.find(OrderItem.class, orderItem.getId());

		assertThat(removedOrderItem).isNull();
	}
}