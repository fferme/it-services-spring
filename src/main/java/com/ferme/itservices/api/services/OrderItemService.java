package com.ferme.itservices.api.services;

import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.api.dtos.mappers.OrderItemMapper;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.OrderItem;
import com.ferme.itservices.api.repositories.OrderItemRepository;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        existingOrderItem.setOrderItemType(OrderItemTypeConverter.convertOrderItemTypeValue(updatedOrderItem.getOrderItemType().getValue()));
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

    public void exportDataToOrderItem() {
        try {
            InputStream stream = new FileInputStream("src/main/resources/entities/orderItems.json");
            JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Gson gson = new Gson();

            reader.beginArray();
            while (reader.hasNext()) {
                OrderItem orderItem = gson.fromJson(reader, OrderItem.class);
                orderItemRepository.save(orderItem);
            }
            reader.endArray();
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}