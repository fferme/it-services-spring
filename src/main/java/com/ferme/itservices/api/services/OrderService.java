package com.ferme.itservices.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.ferme.itservices.ocrreader.OCRRestAPI;
import com.ferme.itservices.ocrreader.OCRService;
import com.ferme.itservices.ocrreader.file.FileConverter;
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

@Validated
@Service
@AllArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ClientRepository clientRepository;
	private final OrderItemRepository orderItemRepository;

	public List<OrderDTO> listAll() {
		return OrderMapper.toOrderDTOList(orderRepository.findAll());
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public OrderDTO findById(@Valid @NotNull UUID id) {
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(Order.class, id.toString()));

		return OrderMapper.toOrderDTO(order);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public OrderDTO create(OrderDTO orderDTO) {
		Client client = orderDTO.clientDTO().id() != null
			? clientRepository.findById(orderDTO.clientDTO().id())
			.orElseThrow(() -> new EntityNotFoundException("Client not found with given ID"))
			: clientRepository.save(ClientMapper.toClient(orderDTO.clientDTO()));

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
				: orderItemRepository.save(OrderItemMapper.toOrderItem(orderItemDTO));

			orderItems.add(orderItem);
		}
		order.setOrderItems(orderItems);

		Order savedOrder = orderRepository.save(order);
		return OrderMapper.toOrderDTO(savedOrder);
	}

	@Transactional(propagation = Propagation.REQUIRED)
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
	public void deleteById(@NotNull UUID id) {
		orderRepository.deleteById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAll() {
		orderRepository.deleteAll();
	}

	public void importOrders() {
		OCRRestAPI ocrRestAPI = OCRRestAPI.getInstance(new FileConverter(new OCRService()), new ObjectMapper());
		ocrRestAPI.extractTextFromJPG("./Marco.jpg");
	}
}