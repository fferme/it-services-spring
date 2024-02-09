package com.ferme.itservices.api.services;

import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.api.dtos.mappers.OrderItemMapper;
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
    private final OrderItemMapper orderItemMapper;

    public List<OrderItemDTO> listAll() {
        List<OrderItem> clients = orderItemRepository.findAll();

        return clients.stream()
                      .map(orderItemMapper::toDTO)
                      .sorted(Comparator.comparing(OrderItemDTO::getDescription))
                      .collect(Collectors.toList());
    }

    public OrderItemDTO findById(@Valid @NotNull UUID id) {
        return orderItemRepository.findById(id).map(orderItemMapper::toDTO)
                               .orElseThrow(RuntimeException::new);
    }

    public OrderItemDTO create(@Valid @NotNull OrderItemDTO OrderItemDTO) {
        return orderItemMapper.toDTO(orderItemRepository.save(orderItemMapper.toEntity(OrderItemDTO)));
    }

    public OrderItemDTO update(@NotNull UUID id, @Valid @NotNull OrderItemDTO newOrderItemDTO) {
        return orderItemRepository.findById(id)
                               .map(orderItemFound -> {
                                   orderItemFound.setOrderItemType(newOrderItemDTO.getOrderItemType());
                                   orderItemFound.setDescription(newOrderItemDTO.getDescription());
                                   orderItemFound.setCashPrice(orderItemFound.getCashPrice());
                                   orderItemFound.setInstallmentPrice(orderItemFound.getInstallmentPrice());
                                   orderItemFound.setIsPayed(newOrderItemDTO.getIsPayed());

                                   return orderItemMapper.toDTO(orderItemRepository.save(orderItemFound));

                               }).orElseThrow(RuntimeException::new);
    }

    public void deleteById(@NotNull UUID id) {
        orderItemRepository.delete(orderItemRepository.findById(id)
                                                .orElseThrow(RuntimeException::new));
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
                OrderItemDTO orderItemDTO = orderItemMapper.toDTO(gson.fromJson(reader, OrderItem.class));
                orderItemRepository.save(orderItemMapper.toEntity(orderItemDTO));
            }
            reader.endArray();
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}