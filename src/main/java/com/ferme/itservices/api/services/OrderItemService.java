package com.ferme.itservices.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.OrderItem;
import com.ferme.itservices.api.repositories.OrderItemRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
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

    public OrderItem findById(@Valid @NotNull UUID id) {
        return orderItemRepository.findById(id)
                                  .orElseThrow(() -> new RecordNotFoundException(OrderItem.class, id));
    }

    public OrderItem create(@Valid @NotNull OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public OrderItem update(@NotNull UUID id, @Valid @NotNull OrderItem updatedOrderItem) {
        OrderItem existingOrderItem = findById(id);
        existingOrderItem.setOrderItemType(
            OrderItemTypeConverter.convertOrderItemTypeValue(updatedOrderItem.getOrderItemType().getValue()));
        existingOrderItem.setDescription(updatedOrderItem.getDescription());
        existingOrderItem.setCashPrice(updatedOrderItem.getCashPrice());
        existingOrderItem.setInstallmentPrice(updatedOrderItem.getInstallmentPrice());
        return orderItemRepository.save(existingOrderItem);
    }

    public void deleteById(@NotNull UUID id) {
        orderItemRepository.deleteById(id);
    }

    public void deleteAll() {
        orderItemRepository.deleteAll();
    }

    public void exportDataToOrderItem() throws IOException {
        orderItemRepository.saveAll(readJsonData("src/main/resources/entities/orderItems.json"));
    }

    public List<OrderItem> readJsonData(String filePath) {
        List<OrderItem> orderItems = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File path = new File(filePath);
            JsonNode jsonArrayNode = objectMapper.readTree(path);

            if (jsonArrayNode.isArray()) {
                ArrayNode arrayNode = (ArrayNode) jsonArrayNode;

                for (JsonNode orderItemNode: arrayNode) {
                    String orderItemTypeRaw = orderItemNode.get("orderItemType").asText();
                    OrderItemType orderItemType = OrderItemTypeConverter.convertOrderItemTypeValue(orderItemTypeRaw);
                    String description = orderItemNode.get("description").asText();
                    Double cashPrice = orderItemNode.get("cashPrice").asDouble();
                    Double installmentPrice = orderItemNode.get("installmentPrice").asDouble();

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderItemType(orderItemType);
                    orderItem.setDescription(description);
                    orderItem.setCashPrice(cashPrice);
                    orderItem.setInstallmentPrice(installmentPrice);

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