package com.fermesolutions.itservices.controller;

import com.fermesolutions.itservices.model.OrderItem;
import com.fermesolutions.itservices.service.OrderItemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api/order-items")
@AllArgsConstructor
public class OrderItemController {
    private OrderItemService orderItemService;

    @GetMapping
    public @ResponseBody List<OrderItem> listAll() {
        return orderItemService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> findById(@PathVariable @NotNull @Positive Long id) {
        return orderItemService.findById(id)
            .map(orderItemFound -> ResponseEntity.ok().body(orderItemFound))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderItem create(@RequestBody @Valid OrderItem orderItem) {
        return orderItemService.create(orderItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> update(@PathVariable @NotNull @Positive Long id, 
        @RequestBody @Valid OrderItem newOrderItem) {
        return orderItemService.update(id, newOrderItem)
            .map(orderItemFound -> ResponseEntity.ok().body(orderItemFound))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (orderItemService.delete(id)) {
            return ResponseEntity.noContent().<Void>build();
        }
        return ResponseEntity.notFound().build();
    }

}
