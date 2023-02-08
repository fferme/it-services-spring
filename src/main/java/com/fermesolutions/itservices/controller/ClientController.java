package com.fermesolutions.itservices.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.fermesolutions.itservices.dto.ClientDTO;
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
    public @ResponseBody List<ClientDTO> listAll() {
        return clientService.listAll();
    }

    @GetMapping("/{id}")
    public ClientDTO findById(@PathVariable @NotNull @Positive Long id) {
        return clientService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ClientDTO create(@RequestBody @Valid @NotNull ClientDTO clientDTO) {
        return clientService.create(clientDTO);
    }

    @PutMapping("/{id}")
    public ClientDTO update(@PathVariable @Positive @NotNull Long id, 
            @RequestBody @Valid @NotNull ClientDTO newClientDTO) {
        return clientService.update(id, newClientDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull @Positive Long id) {
        clientService.delete(id);
    }

}
