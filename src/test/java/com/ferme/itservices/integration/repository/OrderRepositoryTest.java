package com.ferme.itservices.integration.repository;

import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.ferme.itservices.common.OrderConstants.INVALID_ORDER;
import static com.ferme.itservices.common.OrderConstants.VALID_ORDER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
public class OrderRepositoryTest {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void createOrder_WithValidData_ReturnsOrder() {
		Order order = orderRepository.save(VALID_ORDER);

		Order sut = testEntityManager.find(Order.class, order.getId());

		assertThat(sut).isNotNull();
		assertThat(sut.getProblems()).isEqualTo(order.getProblems());
		assertThat(sut.getDeviceName()).isEqualTo(order.getDeviceName());
		assertThat(sut.getDeviceSN()).isEqualTo(order.getDeviceSN());
		assertThat(sut.getClient()).isEqualTo(order.getClient());
		assertThat(sut.getOrderItems()).isEqualTo(order.getOrderItems());
		assertThat(sut.getTotalPrice()).isEqualTo(order.getTotalPrice());
	}

	@Test
	public void createOrder_WithInvalidData_ThrowsException() {
		Order emptyOrder = new Order();
		assertThatThrownBy(() -> orderRepository.save(emptyOrder)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> orderRepository.save(INVALID_ORDER)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void createOrder_WithExistingId_ThrowsException() {
		Order order = testEntityManager.persistFlushFind(VALID_ORDER);
		testEntityManager.detach(order);
		order.setId(null);

		assertThatThrownBy(() -> orderRepository.save(order)).isInstanceOf(RuntimeException.class);
	}
}