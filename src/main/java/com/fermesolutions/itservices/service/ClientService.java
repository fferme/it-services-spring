package com.fermesolutions.itservices.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import com.fermesolutions.itservices.exception.RecordNotFoundException;
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

    public Client findById(@PathVariable @NotNull @Positive Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new RecordNotFoundException(clientId));
    }

    public Client create(@Valid Client client) {
        return clientRepository.save(client);
    }

    public Client update(@NotNull Long clientId, @Valid Client newClient) {
        return clientRepository.findById(clientId)
                .map(clientFound -> {
                    clientFound.setName(newClient.getName());
                    clientFound.setGender(newClient.getGender());
                    clientFound.setPhoneNumber(newClient.getPhoneNumber());
                    clientFound.setNeighbourhood(newClient.getNeighbourhood());
                    clientFound.setReference(newClient.getReference());

                    return clientRepository.save(clientFound);
                }).orElseThrow(() -> new RecordNotFoundException(clientId));
    }

    public void delete(@PathVariable @NotNull @Positive Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RecordNotFoundException(clientId));
        if (!client.getOrders().isEmpty()) {
            clientRepository.removeClientFromOrders(clientId);
        }
        
        clientRepository.delete(clientRepository.findById(clientId)
                .orElseThrow(() -> new RecordNotFoundException(clientId)));
    }

}
