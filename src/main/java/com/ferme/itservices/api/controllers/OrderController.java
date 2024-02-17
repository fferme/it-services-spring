package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.dtos.OrderDTO;
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
    public List<OrderDTO> listAll() {
        return orderService.listAll();
    }

    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable @NotNull UUID id) {
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderDTO create(@RequestBody @Valid @NotNull OrderDTO orderDTO) {
        return orderService.create(orderDTO);
    }

    @PutMapping("/{id}")
    public OrderDTO update(@PathVariable @NotNull UUID id, @RequestBody @Valid @NotNull OrderDTO newOrderDTO) {
        return orderService.update(id, newOrderDTO);
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