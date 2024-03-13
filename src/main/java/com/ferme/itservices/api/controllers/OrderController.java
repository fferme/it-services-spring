package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.api.services.OrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@Transactional
@CrossOrigin(origins = "*")
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public List<Order> listAll() {
        return orderService.listAll();
    }

    @GetMapping("/{id}")
    public Order findById(@PathVariable @NotNull UUID id) {
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Order create(@RequestBody @Valid @NotNull Order order) {
        return orderService.create(order);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable @NotNull UUID id, @RequestBody @Valid @NotNull Order newOrder) {
        return orderService.update(id, newOrder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull UUID id) {
        orderService.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAll() {
        orderService.deleteAll();
    }
}
