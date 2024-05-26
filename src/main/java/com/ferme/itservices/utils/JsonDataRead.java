package com.ferme.itservices.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.models.OrderItem;
import lombok.Generated;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Generated
public abstract class JsonDataRead {
	public static List<Client> readClientsJsonData() {
		List<Client> clients = new ArrayList<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			File path = new File("src/main/resources/entities/clients.json");
			JsonNode jsonArrayNode = objectMapper.readTree(path);

			if (jsonArrayNode.isArray()) {
				ArrayNode arrayNode = (ArrayNode) jsonArrayNode;

				for (JsonNode clientNode : arrayNode) {
					String name = clientNode.get("name").asText();
					String phoneNumber = clientNode.get("phoneNumber").asText();
					String neighborhood = clientNode.get("neighborhood").asText();
					String address = clientNode.get("address").asText();
					String reference = clientNode.get("reference").asText();

					Client client = new Client();
					client.setName(name);
					client.setPhoneNumber(phoneNumber);
					client.setNeighborhood(neighborhood);
					client.setAddress(address);
					client.setReference(reference);

					clients.add(client);
				}
			} else {
				System.out.println("File does not contain a JSON array");
			}
		} catch (IOException e) {
			System.out.println("Error when reading JSON array: " + e.getMessage());
		}

		return clients;
	}

	public static List<OrderItem> readOrderItemsJsonData() {
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
}
