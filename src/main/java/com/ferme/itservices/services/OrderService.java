package com.ferme.itservices.services;

import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.models.Order;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@Service
@AllArgsConstructor
public class OrderService {
	private EntityManager entityManager;

	private final OrderRepository orderRepository;

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Order> listAll() {
		return orderRepository.findAll();
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Optional<Order> findById(@Valid @NotNull UUID id) {
		return orderRepository.findById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Order create(Order order) {
		Client client = entityManager.merge(order.getClient());
		order.setClient(client);

		List<OrderItem> orderItems = order.getOrderItems();
		List<OrderItem> newOrderItems = new ArrayList<>();
		for (OrderItem orderItem : orderItems) {
			orderItem = entityManager.merge(orderItem);
			newOrderItems.add(orderItem);
		}
		order.setOrderItems(newOrderItems);

		return orderRepository.save(order);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Order update(@NotNull UUID id, @Valid @NotNull Order updatedOrder) {
		return orderRepository.findById(id)
			.map(orderFound -> {
				orderFound.setDeviceName(updatedOrder.getDeviceName());
				orderFound.setDeviceSN(updatedOrder.getDeviceSN());
				orderFound.setProblems(updatedOrder.getProblems());
				orderFound.setClient(updatedOrder.getClient());
				orderFound.setOrderItems(updatedOrder.getOrderItems());

				return orderRepository.save(orderFound);

			}).orElseThrow(() -> new RecordNotFoundException(Order.class, id.toString()));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteById(@NotNull UUID id) {
		orderRepository.deleteById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAll() {
		orderRepository.deleteAll();
	}
}