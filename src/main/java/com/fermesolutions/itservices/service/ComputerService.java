package com.fermesolutions.itservices.service;

import com.fermesolutions.itservices.model.Computer;
import com.fermesolutions.itservices.repository.ComputerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Validated
@Service
public class ComputerService {

    private final ComputerRepository computerRepository;

    public ComputerService(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    public List<Computer> listAll() {
        return computerRepository.findAll();
    }

    public Optional<Computer> findById(@PathVariable @NotNull @Positive Long id) {
        return computerRepository.findById(id);
    }

    public Computer create(@Valid Computer course) {
        return computerRepository.save(course);
    }

    public Optional<Computer> update(@NotNull @Positive Long id, @Valid Computer newComputer) {
        return computerRepository.findById(id)
                .map(computerFound -> {
                    computerFound.setComputerType(newComputer.getComputerType());
                    computerFound.setOsType(newComputer.getOsType());
                    computerFound.setCpu(newComputer.getCpu());
                    computerFound.setRam(newComputer.getRam());
                    computerFound.setGpu(newComputer.getGpu());

                    return computerRepository.save(computerFound);
                });
    }

    public boolean delete(@PathVariable @NotNull @Positive Long id) {
        return computerRepository.findById(id)
                .map(recordFound -> {
                    computerRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }


}
