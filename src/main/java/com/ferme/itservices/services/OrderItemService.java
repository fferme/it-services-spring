package com.ferme.itservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.models.OrderItem;
import com.ferme.itservices.repositories.OrderItemRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemService {
	private final OrderItemRepository orderItemRepository;

	public List<OrderItem> listAll() {
		return orderItemRepository.findAll()
			.stream()
			.sorted(Comparator.comparing(OrderItem::getDescription))
			.collect(Collectors.toList());
	}

	public OrderItem create(@Valid @NotNull OrderItem orderItem) {
		return orderItemRepository.save(orderItem);
	}

	public Optional<OrderItem> findById(@Valid @NotNull Long id) {
		return orderItemRepository.findById(id);
	}

	public void deleteById(@NotNull Long id) {
		orderItemRepository.deleteById(id);
	}

	public void deleteAll() {
		orderItemRepository.deleteAll();
	}

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
					OrderItemType orderItemType = OrderItemTypeConverter.convertOrderItemTypeValue(orderItemTypeRaw);
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

	public List<OrderItem> exportDataToOrderItem() throws IOException {
		return orderItemRepository.saveAll(readJsonData());
	}

	public OrderItem update(@NotNull Long id, @Valid @NotNull OrderItem updatedOrderItem) {
		return orderItemRepository.findById(id)
			.map(orderItemFound -> {
				orderItemFound.setOrderItemType(OrderItemTypeConverter.convertOrderItemTypeValue(
				updatedOrderItem.getOrderItemType().getValue()));
				orderItemFound.setDescription(updatedOrderItem.getDescription());
				orderItemFound.setPrice(updatedOrderItem.getPrice());

				return orderItemRepository.save(orderItemFound);

			}).orElseThrow(() -> new RecordNotFoundException(Client.class, id.toString()));
	}
}