package com.ferme.itservices.api.services;

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
import com.ferme.itservices.ocrreader.file.FileUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Validated
@Service
@AllArgsConstructor
@Slf4j
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

	public void importOrders(String dir) {
		FileUtils fileUtils = FileUtils.getInstance();

		Path dirPath = Paths.get(dir);
		if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.txt")) {
				for (Path entry : stream) {
					log.info("Processing file: {}", entry.toAbsolutePath());
					String content = new String(Files.readAllBytes(entry));
					log.info("Nome: {}", fileUtils.findAndExtractValor(content, "Nome do(a) solicitante:"));
					log.info("Celular: {}", fileUtils.findAndExtractValor(content, "Celular:"));
					log.info("Data: {}", fileUtils.findAndExtractValor(content, "Data:"));
				}
			} catch (IOException e) {
				log.error("Error accessing folder: {}", e.getMessage());
			}
		} else {
			log.error("The specified folder does not exist or is not a directory");
		}
	}
}