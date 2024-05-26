package com.ferme.itservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ferme.itservices.dtos.OrderItemDTO;
import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.OrderItemRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ferme.itservices.dtos.mappers.OrderItemMapper.*;

@Service
@AllArgsConstructor
public class OrderItemService {
	private final OrderItemRepository orderItemRepository;

	@Transactional
	public List<OrderItemDTO> listAll() {
		List<OrderItem> orderItems = orderItemRepository.findAll();

		return toOrderItemDTOList(
			orderItems.stream()
				.sorted(Comparator.comparing(OrderItem::getDescription))
				.collect(Collectors.toList())
		);
	}

	public OrderItemDTO findById(@Valid @NotNull UUID id) {
		OrderItem orderItem = orderItemRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(OrderItem.class, id.toString()));

		return toOrderItemDTO(orderItem);
	}

	public OrderItemDTO findByDescription(@NotBlank String description) {
		OrderItem orderItem = orderItemRepository.findByDescription(description)
			.orElseThrow(() -> new RecordNotFoundException(OrderItem.class, description));

		return toOrderItemDTO(orderItem);
	}

	public OrderItemDTO create(OrderItemDTO orderItemDTO) {
		OrderItem orderItem = toOrderItem(orderItemDTO);

		return toOrderItemDTO(orderItemRepository.save(orderItem));
	}

	public OrderItemDTO update(@NotNull UUID id, @Valid @NotNull OrderItemDTO updatedOrderItemDTO) {
		return orderItemRepository.findById(id)
			.map(orderItemFound -> {
				orderItemFound.setOrderItemType(OrderItemTypeConverter.convertToOrderItemType(
					updatedOrderItemDTO.orderItemType().getValue())
				);
				orderItemFound.setDescription(updatedOrderItemDTO.description());
				orderItemFound.setPrice(updatedOrderItemDTO.price());

				return toOrderItemDTO(orderItemRepository.save(orderItemFound));

			}).orElseThrow(() -> new RecordNotFoundException(OrderItem.class, id.toString()));
	}

	public void deleteById(@NotNull UUID id) {
		orderItemRepository.deleteById(id);
	}

	public void deleteAll() {
		orderItemRepository.deleteAll();
	}

	@Generated
	private static List<OrderItem> readJsonData() {
		List<OrderItem> orderItems = new ArrayList<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			File path = new File("src/main/resources/entities/orderItems.json");
			JsonNode jsonArrayNode = objectMapper.readTree(path);

			if (jsonArrayNode.isArray()) {
				ArrayNode arrayNode = (ArrayNode) jsonArrayNode;

				for (JsonNode orderItemNode : arrayNode) {
					String orderItemTypeRaw = orderItemNode.get("orderItemType").asText();
					OrderItemType orderItemType = OrderItemTypeConverter.convertToOrderItemType(orderItemTypeRaw);
					String description = orderItemNode.get("description").asText();
					Double price = orderItemNode.get("price").asDouble();

					OrderItem orderItem = new OrderItem();
					orderItem.setOrderItemType(orderItemType);
					orderItem.setDescription(description);
					orderItem.setPrice(price);

					orderItems.add(orderItem);
				}
			} else {
				System.out.println("File does not contain a JSON array");
			}
		} catch (IOException e) {
			System.out.println("Error when reading JSON array: " + e.getMessage());
		}

		return orderItems;
	}

	@Generated
	public List<OrderItemDTO> exportDataToOrderItem() throws IOException {
		return toOrderItemDTOList(orderItemRepository.saveAll(readJsonData()));
	}
}