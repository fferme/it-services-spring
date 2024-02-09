package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.dtos.OrderItemDTO;
import com.ferme.itservices.api.services.OrderItemService;
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
@RequestMapping("/api/orderItems")
public class OrderItemController {
    private OrderItemService orderItemService;

    @GetMapping
    public List<OrderItemDTO> listAll() {
        return orderItemService.listAll();
    }

    @GetMapping("/{id}")
    public OrderItemDTO findById(@PathVariable @NotNull UUID id) {
        return orderItemService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderItemDTO create(@RequestBody @Valid @NotNull OrderItemDTO orderItemDTO) {
        return orderItemService.create(orderItemDTO);
    }

    @PutMapping("/{id}")
    public OrderItemDTO update(@PathVariable @NotNull UUID id, @RequestBody @Valid @NotNull OrderItemDTO newOrderItemDTO) {
        return orderItemService.update(id, newOrderItemDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull UUID id) {
        orderItemService.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAll() {
        orderItemService.deleteAll();
    }

    @PostMapping("/import")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void importClients() {
        orderItemService.exportDataToOrderItem();
    }
}