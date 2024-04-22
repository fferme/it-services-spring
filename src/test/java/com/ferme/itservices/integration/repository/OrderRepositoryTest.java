package com.ferme.itservices.integration.repository;

import com.ferme.itservices.models.Order;
import com.ferme.itservices.repositories.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.common.OrderConstants.INVALID_ORDER;
import static com.ferme.itservices.common.OrderConstants.ORDER_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class OrderRepositoryTest {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@AfterEach
	public void nullifyIds() {
		ORDER_A.setId(null);
		ORDER_A.getClient().setId(null);
		ORDER_A.getOrderItems().forEach(orderItem -> orderItem.setId(null));
	}

	@Test
	public void createOrder_WithValidData_ReturnsOrder() {
		Order order = orderRepository.save(ORDER_A);

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
		Order order = testEntityManager.persistFlushFind(ORDER_A);
		testEntityManager.detach(order);
		order.setId(null);

		assertThatThrownBy(() -> orderRepository.save(order)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getOrder_ByExistingId_ReturnsOrder() {
		Order savedOrder = testEntityManager.persistFlushFind(ORDER_A);

		Optional<Order> orderOpt = orderRepository.findById(savedOrder.getId());

		assertThat(orderOpt).isNotEmpty();
		assertThat(orderOpt.orElse(null)).isEqualTo(savedOrder);
	}

	@Test
	public void getOrder_ByUnexistingId_ReturnsEmpty() {
		Optional<Order> orderOpt = orderRepository.findById(1L);

		assertThat(orderOpt).isEmpty();
	}

	@Sql(
		scripts = {
			"/sql_scripts/import_clients.sql",
			"/sql_scripts/import_orderItems.sql",
			"/sql_scripts/import_orders.sql"
		})
	@Test
	public void listOrders_ReturnsOrders() throws Exception {
		List<Order> orders = orderRepository.findAll();

		assertThat(orders).isNotEmpty();
		assertThat(orders).hasSize(3);
	}

	@Test
	public void listOrders_ReturnsNoOrders() throws Exception {
		List<Order> orders = orderRepository.findAll();

		assertThat(orders).isEmpty();
	}

}