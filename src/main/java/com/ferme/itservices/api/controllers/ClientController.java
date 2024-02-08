package com.ferme.itservices.api.controllers;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.services.ClientService;
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
@RequestMapping("/api/clients")
public class ClientController {
    private ClientService clientService;

    @GetMapping
    public List<ClientDTO> listAll() {
        return clientService.listAll();
    }

    @GetMapping("/{id}")
    public ClientDTO findById(@PathVariable @NotNull UUID id) {
        return clientService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ClientDTO create(@RequestBody @Valid @NotNull ClientDTO clientDTO) {
        return clientService.create(clientDTO);
    }

    @PutMapping("/{id}")
    public ClientDTO update(@PathVariable @NotNull UUID id, @RequestBody @Valid @NotNull ClientDTO newClientDTO) {
        return clientService.update(id, newClientDTO);
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
    public void importClients() {
        clientService.importClients();
    }
}