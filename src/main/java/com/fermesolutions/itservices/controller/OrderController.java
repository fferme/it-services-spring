package com.fermesolutions.itservices.controller;

import com.fermesolutions.itservices.model.Client;
import com.fermesolutions.itservices.model.Order;
import com.fermesolutions.itservices.service.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api/orders")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public @ResponseBody List<Order> listAll() {
        return orderService.listAll();
    }

    @GetMapping("/{orderId}")
    public Order findById(@PathVariable @NotNull @Positive Long orderId) {
        return orderService.findById(orderId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Order create(@RequestBody @Valid Order orderItem) {
        return orderService.create(orderItem);
    }

    @PutMapping("/{orderId}")
    public Order update(@PathVariable @NotNull @Positive Long orderId,
            @RequestBody @Valid Order newOrder) {
        return orderService.update(orderId, newOrder);
    }

    @DeleteMapping("/{orderId}")
    public void delete(@PathVariable Long orderId) {
        orderService.delete(orderId);
    }

    // Deleta o cliente de determinada ordem de serviço
    @DeleteMapping("/{orderId}/clients")
    public void deleteClientFromOrder(@PathVariable Long orderId) {
        orderService.deleteClientFromOrder(orderId);
    }
    
    // Adiciona um cliente existente para determinada ordem de serviço
    @PutMapping("/{orderId}/clients/add/{clientId}")
    @ResponseStatus(code = HttpStatus.OK)
    public Order addClientToOrder(@PathVariable @NotNull @Positive Long orderId, @PathVariable @NotNull @Positive Long clientId){
        return orderService.addClientToOrder(orderId, clientId);
    }
    
    // Atualiza um cliente para determinada ordem de serviço
    @PutMapping("/{orderId}/clients/update/{clientId}")
    public Order updateClientInOrder(@PathVariable @NotNull @Positive Long orderId, @PathVariable @NotNull @Positive Long clientId, 
        @RequestBody @Valid Client newClient) {
        return orderService.updateClientInOrder(orderId, clientId, newClient);
    }

    // Cria um cliente para determinada ordem de serviço
    @PostMapping("/{orderId}/clients")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Order addClientToOrder(@PathVariable @Positive Long orderId, @Valid @RequestBody Client client) {
        return orderService.createClientToOrder(orderId, client);
    }
}
