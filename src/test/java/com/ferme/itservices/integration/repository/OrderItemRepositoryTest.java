package com.ferme.itservices.integration.repository;

import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.OrderItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static com.ferme.itservices.common.OrderItemConstants.INVALID_ORDERITEM;
import static com.ferme.itservices.common.OrderItemConstants.VALID_ORDERITEM;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
public class OrderItemRepositoryTest {
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@AfterEach
	public void nullifyId() {
		VALID_ORDERITEM.setId(null);
	}

	@Test
	public void createOrderItem_WithValidData_ReturnsOrderItem() {
		OrderItem orderItem = orderItemRepository.save(VALID_ORDERITEM);

		OrderItem sut = testEntityManager.find(OrderItem.class, orderItem.getId());

		assertThat(sut).isNotNull();
		assertThat(sut.getOrderItemType()).isEqualTo(VALID_ORDERITEM.getOrderItemType());
		assertThat(sut.getDescription()).isEqualTo(VALID_ORDERITEM.getDescription());
		assertThat(sut.getPrice()).isEqualTo(VALID_ORDERITEM.getPrice());
	}

	@Test
	public void createOrderItem_WithInvalidData_ThrowsException() {
		OrderItem emptyOrderItem = new OrderItem();
		assertThatThrownBy(() -> orderItemRepository.save(emptyOrderItem)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> orderItemRepository.save(INVALID_ORDERITEM)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void createOrderItem_WithExistingDescription_ThrowsException() {
		OrderItem orderItem = testEntityManager.persistFlushFind(VALID_ORDERITEM);
		testEntityManager.detach(orderItem);
		orderItem.setId(null);

		assertThatThrownBy(() -> orderItemRepository.save(orderItem)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getOrderItem_ByExistingId_ReturnsOrderItem() {
		OrderItem orderItem = testEntityManager.persistFlushFind(VALID_ORDERITEM);

		Optional<OrderItem> orderItemOpt = orderItemRepository.findById(orderItem.getId());

		assertThat(orderItemOpt).isNotEmpty();
		assertThat(orderItemOpt.orElse(null)).isEqualTo(orderItem);
	}

	@Test
	public void getOrderItem_ByUnexistingId_ReturnsEmpty() {
		Optional<OrderItem> orderItemOpt = orderItemRepository.findById(1L);

		assertThat(orderItemOpt).isEmpty();
	}
}