package com.ferme.itservices.api.services;

import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.repositories.OrderRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
@Service
@AllArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;

	public List<Order> listAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> findById(@Valid @NotNull Long id) {
		return orderRepository.findById(id);
	}

	public Order create(@Valid @NotNull Order order) {
		return orderRepository.save(order);
	}

	public Order update(@NotNull Long id, @Valid @NotNull Order updatedOrder) {
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

	public void deleteById(@NotNull Long id) {
		orderRepository.deleteById(id);
	}

	public void deleteAll() {
		orderRepository.deleteAll();
	}
}