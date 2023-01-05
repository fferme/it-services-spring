package com.fermesolutions.itservices.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import com.fermesolutions.itservices.model.Client;
import com.fermesolutions.itservices.repository.ClientRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> listAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(@PathVariable @NotNull @Positive Long id) {
        return clientRepository.findById(id);
    }

    public Client create(@Valid Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> update(@NotNull @Positive Long id, @Valid Client newClient) {
        return clientRepository.findById(id)
                .map(clientFound -> {
                    clientFound.setName(newClient.getName());
                    clientFound.setGender(newClient.getGender());
                    clientFound.setPhoneNumber(newClient.getPhoneNumber());
                    clientFound.setDistrict(newClient.getDistrict());
                    clientFound.setReference(newClient.getReference());

                    return clientRepository.save(clientFound);
                });
    }

    public boolean delete(@PathVariable @NotNull @Positive Long id) {
        return clientRepository.findById(id)
                .map(recordFound -> {
                    clientRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
    
}
