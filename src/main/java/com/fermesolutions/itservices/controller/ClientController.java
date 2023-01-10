package com.fermesolutions.itservices.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fermesolutions.itservices.model.Client;
import com.fermesolutions.itservices.service.ClientService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public @ResponseBody List<Client> listAll() {
        return clientService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable @NotNull @Positive UUID id) {
        return clientService.findById(id)
            .map(clientFound -> ResponseEntity.ok().body(clientFound))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Client create(@RequestBody @Valid Client client) {
        return clientService.create(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable @NotNull @Positive UUID id, 
        @RequestBody @Valid Client newClient) {
        return clientService.update(id, newClient)
            .map(recordFound -> ResponseEntity.ok().body(recordFound))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (clientService.delete(id)) {
            return ResponseEntity.noContent().<Void>build();
        }
        return ResponseEntity.notFound().build();
    }

}
