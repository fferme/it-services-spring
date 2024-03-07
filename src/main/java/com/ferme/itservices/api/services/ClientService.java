package com.ferme.itservices.api.services;

import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.repositories.ClientRepository;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> listAll() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                      .sorted(Comparator.comparing(Client::getName))
                      .collect(Collectors.toList());
    }

    public Optional<Client> findById(@Valid @NotNull UUID id) {
        return clientRepository.findById(id);
    }

    public Client create(@Valid @NotNull Client client) {
        return clientRepository.save(client);
    }

    public Client update(@NotNull UUID id, @Valid @NotNull Client newClient) {
        return clientRepository.findById(id)
                               .map(clientFound -> {
                                   clientFound.setNeighborhood(newClient.getNeighborhood());
                                   clientFound.setAddress(newClient.getAddress());
                                   clientFound.setReference(newClient.getReference());

                                   return clientRepository.save(clientFound);

                               }).orElseThrow(() -> new RecordNotFoundException(Client.class, id));
    }

    public void deleteById(@NotNull UUID id) {
        clientRepository.deleteById(id);
    }

    public void deleteAll() {
        clientRepository.deleteAll();
    }

    public void exportDataToClient() throws IOException {
        InputStream stream = new FileInputStream("src/main/resources/entities/clients.json");
        JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        Gson gson = new Gson();

        reader.beginArray();
        while (reader.hasNext()) {
            Client client = gson.fromJson(reader, Client.class);
            clientRepository.save(client);
        }
        reader.endArray();
        reader.close();
    }
}