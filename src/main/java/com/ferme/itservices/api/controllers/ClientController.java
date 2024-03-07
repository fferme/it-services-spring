package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.services.ClientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@Transactional
@CrossOrigin(origins = "*")
@RequestMapping("/api/clients")
public class ClientController {
    private ClientService clientService;

    @GetMapping
    public List<Client> listAll() {
        return clientService.listAll();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable @NotNull UUID id) {
        return clientService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Client create(@RequestBody @Valid @NotNull Client client) {
        return clientService.create(client);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable @NotNull UUID id, @RequestBody @Valid @NotNull Client newClient) {
        return clientService.update(id, newClient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull UUID id) {
        clientService.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAll() {
        clientService.deleteAll();
    }

    @PostMapping("/import")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void importClients() throws IOException {
        clientService.exportDataToClient();
    }
}