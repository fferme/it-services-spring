package com.fermesolutions.itservices.service;

import com.fermesolutions.itservices.dto.ClientDTO;
import com.fermesolutions.itservices.dto.mapper.ClientMapper;
import com.fermesolutions.itservices.exception.RecordNotFoundException;
import com.fermesolutions.itservices.model.Client;
import com.fermesolutions.itservices.model.Order;
import com.fermesolutions.itservices.repository.ClientRepository;
import com.fermesolutions.itservices.repository.OrderRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public OrderService(OrderRepository orderRepository, ClientService clientService, ClientRepository clientRepository, ClientMapper clientMapper) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    public Order findById(@PathVariable @NotNull @Positive Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RecordNotFoundException(orderId));
    }

    public Order create(@Valid Order order) {
        return orderRepository.save(order);
    }

    // Cria um cliente e o adiciona para determinada ordem de serviço
    public Order createClientToOrder(@NotNull @Positive Long orderId, @Valid Client client) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RecordNotFoundException(orderId));
        if (order != null) {
            order.setClient(client);
            orderRepository.save(order);
        }
        return order;
    }

    // Adiciona um cliente existente para determinada ordem de serviço
    public Order addClientToOrder(@NotNull @Positive Long orderId, @NotNull @Positive Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RecordNotFoundException(clientId));

        return orderRepository.findById(orderId)
            .map(orderFound -> {
                orderFound.setClient(client);

                return orderRepository.save(orderFound);
            }).orElseThrow(() -> new RecordNotFoundException(clientId));
    }

    public Order update(@NotNull @Positive Long orderId, @Valid Order newOrder) {
        return orderRepository.findById(orderId)
                .map(orderFound -> {
                    orderFound.setClient(newOrder.getClient());
                    orderFound.setComputer(newOrder.getComputer());
                    orderFound.setOrderItems(newOrder.getOrderItems());
                    orderFound.setIssues(newOrder.getIssues());
                    orderFound.setNotes(newOrder.getNotes());

                    return orderRepository.save(orderFound);
                }).orElseThrow(() -> new RecordNotFoundException(orderId));
    }

    // Atualiza um cliente de determinada ordem de serviço
    public Order updateClientInOrder(@NotNull @Positive Long orderId, @NotNull @Positive Long clientId, @Valid ClientDTO clientDTO) {
        clientService.update(clientId, clientDTO);
        
        return orderRepository.findById(orderId)
                .map(orderFound -> {
                    orderFound.setClient(clientMapper.toEntity(clientDTO));

                    return orderRepository.save(orderFound);
                }).orElseThrow(() -> new RecordNotFoundException(orderId));
    }

    public void delete(@PathVariable @NotNull @Positive Long orderId) {
        orderRepository.delete(orderRepository.findById(orderId)
            .orElseThrow(() -> new RecordNotFoundException(orderId)));
    }

    // Deleta um cliente de determinada ordem
    public void deleteClientFromOrder(@PathVariable @NotNull @Positive Long orderId) {
        Order orderFound = orderRepository.findById(orderId).orElseThrow(() -> new RecordNotFoundException(orderId));
        if (orderFound != null) {
            orderFound.setClient(null);
        }
    }
}
