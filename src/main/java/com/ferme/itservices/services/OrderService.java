package com.ferme.itservices.services;

import com.ferme.itservices.dtos.OrderDTO;
import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.models.Order;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.ClientRepository;
import com.ferme.itservices.repositories.OrderItemRepository;
import com.ferme.itservices.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.dtos.mappers.ClientMapper.toClient;
import static com.ferme.itservices.dtos.mappers.OrderItemMapper.toOrderItem;
import static com.ferme.itservices.dtos.mappers.OrderItemMapper.toOrderItemList;
import static com.ferme.itservices.dtos.mappers.OrderMapper.toOrderDTO;
import static com.ferme.itservices.dtos.mappers.OrderMapper.toOrderDTOList;

@Validated
@Service
@AllArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ClientRepository clientRepository;
	private final OrderItemRepository orderItemRepository;

	public List<OrderDTO> listAll() {
		return toOrderDTOList(orderRepository.findAll());
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public OrderDTO findById(@Valid @NotNull UUID id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(Order.class, id.toString()));

		return toOrderDTO(order);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public OrderDTO create(OrderDTO orderDTO) {
		Client client = orderDTO.clientDTO().id() != null
			? clientRepository.findById(orderDTO.clientDTO().id())
			.orElseThrow(() -> new EntityNotFoundException("Client not found with given ID"))
			: clientRepository.save(toClient(orderDTO.clientDTO()));

		Order order = new Order();
		order.setClient(client);
		order.setDeviceName(orderDTO.deviceName());
		order.setDeviceSN(orderDTO.deviceSN());
		order.setIssues(orderDTO.issues());
		order.setTotalPrice(orderDTO.totalPrice());

		List<OrderItem> orderItems = new ArrayList<>();

		for (OrderItemDTO orderItemDTO : orderDTO.orderItemsDTO()) {
			OrderItem orderItem = orderItemDTO.id() != null
				? orderItemRepository.findById(orderItemDTO.id())
				.orElseThrow(() -> new EntityNotFoundException("Order Item not found with given ID"))
				: orderItemRepository.save(toOrderItem(orderItemDTO));

			orderItems.add(orderItem);
		}
		order.setOrderItems(orderItems);

		Order savedOrder = orderRepository.save(order);
		return toOrderDTO(savedOrder);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public OrderDTO update(@NotNull UUID id, @Valid @NotNull OrderDTO updatedOrderDTO) {
		return orderRepository.findById(id)
			.map(orderFound -> {
				orderFound.setDeviceName(updatedOrderDTO.deviceName());
				orderFound.setDeviceSN(updatedOrderDTO.deviceSN());
				orderFound.setIssues(updatedOrderDTO.issues());
				orderFound.setTotalPrice(updatedOrderDTO.totalPrice());
				orderFound.setClient(toClient(updatedOrderDTO.clientDTO()));
				orderFound.setOrderItems(toOrderItemList(updatedOrderDTO.orderItemsDTO()));

				return toOrderDTO(orderRepository.save(orderFound));

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