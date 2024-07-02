package com.ferme.itservices.api.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.dtos.OrderDTO;
import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.api.dtos.mappers.ClientMapper;
import com.ferme.itservices.api.dtos.mappers.OrderItemMapper;
import com.ferme.itservices.api.dtos.mappers.OrderMapper;
import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.models.OrderItem;
import com.ferme.itservices.api.repositories.ClientRepository;
import com.ferme.itservices.api.repositories.OrderItemRepository;
import com.ferme.itservices.api.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.api.dtos.mappers.OrderMapper.toOrderDTOList;
import static com.ferme.itservices.api.utils.JsonDataRead.readJsonData;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
	private final OrderRepository orderRepository;
	private final ClientRepository clientRepository;
	private final OrderItemRepository orderItemRepository;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
	public List<OrderDTO> listAll() {
		return toOrderDTOList(orderRepository.findAll());
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Cacheable(value = "ordersList")
	public OrderDTO findById(@Valid @NotNull UUID id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(Order.class, id.toString()));

		return OrderMapper.toOrderDTO(order);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "ordersList", allEntries = true)
	@CachePut(value = "order", key = "#result.id")
	public OrderDTO create(OrderDTO orderDTO) {
		Client client;
		ClientDTO orderClientDTO = orderDTO.clientDTO();

		if (orderDTO.clientDTO().id() != null) {
			client = clientRepository.findById(orderDTO.clientDTO().id())
				.orElseThrow(() -> new EntityNotFoundException("Client not found with given ID"));
		} else {
			final boolean clientExists = clientRepository.findByNameAndPhoneNumber(orderClientDTO.name(), orderClientDTO.phoneNumber()).isPresent();
			client = clientExists
				? clientRepository.findByNameAndPhoneNumber(orderClientDTO.name(), orderClientDTO.phoneNumber())
				.orElseThrow(() -> new EntityNotFoundException("Client not found with given name and phoneNumber"))
				: clientRepository.save(ClientMapper.toClient(orderDTO.clientDTO()));
		}

		Order order = new Order();
		order.setClient(client);
		order.setDeviceName(orderDTO.deviceName());
		order.setDeviceSN(orderDTO.deviceSN());
		order.setIssues(orderDTO.issues());
		order.setTotalPrice(orderDTO.totalPrice());

		List<OrderItem> orderItems = new ArrayList<>();

		for (OrderItemDTO orderItemDTO : orderDTO.orderItemsDTO()) {
			OrderItem orderItem;

			if (orderItemDTO.id() != null) {
				orderItem = orderItemRepository.findById(orderItemDTO.id())
					.orElseThrow(() -> new EntityNotFoundException("Order item not found with given ID"));
			} else {
				final boolean orderItemExists = orderItemRepository.findByOrderItemTypeAndDescriptionAndPrice(
					orderItemDTO.orderItemType(), orderItemDTO.description(), orderItemDTO.price()).isPresent();
				orderItem = orderItemExists
					? orderItemRepository.findByOrderItemTypeAndDescriptionAndPrice(
						orderItemDTO.orderItemType(), orderItemDTO.description(), orderItemDTO.price()
					)
					.orElseThrow(() -> new EntityNotFoundException("Order item not found with given order item type, description and price"))
					: orderItemRepository.save(OrderItemMapper.toOrderItem(orderItemDTO));
			}

			orderItems.add(orderItem);
		}
		order.setOrderItems(orderItems);

		Order savedOrder = orderRepository.save(order);
		return OrderMapper.toOrderDTO(savedOrder);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "ordersList", allEntries = true)
	@CachePut(value = "order", key = "#id")
	public OrderDTO update(@NotNull UUID id, @Valid @NotNull OrderDTO updatedOrderDTO) {
		return orderRepository.findById(id)
			.map(orderFound -> {
				orderFound.setDeviceName(updatedOrderDTO.deviceName());
				orderFound.setDeviceSN(updatedOrderDTO.deviceSN());
				orderFound.setIssues(updatedOrderDTO.issues());
				orderFound.setTotalPrice(updatedOrderDTO.totalPrice());
				orderFound.setClient(ClientMapper.toClient(updatedOrderDTO.clientDTO()));
				orderFound.setOrderItems(OrderItemMapper.toOrderItemList(updatedOrderDTO.orderItemsDTO()));

				return OrderMapper.toOrderDTO(orderRepository.save(orderFound));

			}).orElseThrow(() -> new RecordNotFoundException(Order.class, id.toString()));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = {"order", "ordersList"}, key = "#id")
	public void deleteById(@NotNull UUID id) {
		orderRepository.deleteById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = {"order", "ordersList"}, allEntries = true)
	public void deleteAll() {
		orderRepository.deleteAll();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<OrderDTO> importOrders() {
		List<OrderDTO> ordersDTO = toOrderDTOList(readJsonData("src/main/resources/json_imports/orders.json", new TypeReference<List<Order>>() { }));
		List<OrderDTO> createdOrdersDTO = new ArrayList<>();

		for (OrderDTO orderDTO : ordersDTO) {
			try {
				createdOrdersDTO.add(create(orderDTO));
			} catch (Exception e) {
				log.error("Error in entity: {}", orderDTO);
				throw new RuntimeException(e);
			}
		}

		return createdOrdersDTO;
	}
}