package com.fermesolutions.itservices.service;

import com.fermesolutions.itservices.model.Client;
import com.fermesolutions.itservices.model.Order;
import com.fermesolutions.itservices.repository.ClientRepository;
import com.fermesolutions.itservices.repository.OrderRepository;
import com.fermesolutions.itservices.service.exceptions.ResourceNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public OrderService(OrderRepository orderRepository, ClientService clientService, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(@PathVariable @NotNull @Positive Long orderId) {
        return orderRepository.findById(orderId);
    }

    public Order create(@Valid Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> update(@NotNull @Positive Long orderId, @Valid Order newOrder) {
        return orderRepository.findById(orderId)
                .map(orderFound -> {
                    orderFound.setClient(newOrder.getClient());
                    orderFound.setComputer(newOrder.getComputer());
                    orderFound.setOrderItems(newOrder.getOrderItems());
                    orderFound.setIssues(newOrder.getIssues());
                    orderFound.setNotes(newOrder.getNotes());

                    return orderRepository.save(orderFound);
                });
    }

    public boolean delete(@PathVariable @NotNull @Positive Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderFound -> {
                    orderRepository.deleteById(orderId);
                    return true;
                })
                .orElse(false);
    }

    public boolean deleteClientFromOrder(@PathVariable @NotNull @Positive Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderFound -> {
                    orderFound.setClient(null);
                    return true;
                })
                .orElse(false);
    }
    
    // Atualiza um cliente de determinada ordem de serviço
    public Optional<Order> updateClientInOrder(@NotNull @Positive Long orderId, @NotNull @Positive Long clientId, @Valid Client client) {
        clientService.update(clientId, client);
        
        return orderRepository.findById(orderId)
                .map(orderFound -> {
                    orderFound.setClient(client);

                    return orderRepository.save(orderFound);
                });
    }

    // Cria um cliente e o adiciona para determinada ordem de serviço
    public Order createClientToOrder(@NotNull @Positive Long orderId, @Valid Client client) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new ResourceNotFoundException("Ordem não encontrada", orderId)
        );
        order.setClient(client);

        return orderRepository.save(order);
    }

    // Adiciona um cliente existente para determinada ordem de serviço
    public Optional<Order> addClientToOrder(@NotNull @Positive Long orderId, @NotNull @Positive Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(
            () -> new ResourceNotFoundException("Cliente não encontrado", clientId)
        );

        return orderRepository.findById(orderId)
            .map(orderFound -> {
                orderFound.setClient(client);

                return orderRepository.save(orderFound);
            });
    }

    // Atualiza cliente para determinada ordem de serviço
    public Optional<Order> updateClientForOrder(@NotNull @Positive Long orderId, @Valid Client client) {
        return orderRepository.findById(orderId)
                .map(orderFound -> {
                    orderFound.setClient(client);

                    return orderRepository.save(orderFound);
                });
    }
}
