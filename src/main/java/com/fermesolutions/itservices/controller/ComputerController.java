package com.fermesolutions.itservices.controller;

    import com.fermesolutions.itservices.model.Computer;
import com.fermesolutions.itservices.service.ComputerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

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

@Validated
@RestController
@RequestMapping(value = "/api/computers")
@AllArgsConstructor
public class ComputerController {
    private final ComputerService computerService;

    @GetMapping
    public @ResponseBody List<Computer> listAll() {
        return computerService.listAll();
    }

    @GetMapping("/{id}")
    public Computer findById(@PathVariable @NotNull @Positive Long id) {
        return computerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Computer create(@RequestBody @Valid Computer client) {
        return computerService.create(client);
    }

    @PutMapping("/{id}")
    public Computer update(@PathVariable @NotNull @Positive Long id, 
        @RequestBody @Valid Computer newComputer) {
        return computerService.update(id, newComputer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        computerService.delete(id);
    }


}
