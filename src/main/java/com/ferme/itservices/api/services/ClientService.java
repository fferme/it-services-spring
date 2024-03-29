package com.ferme.itservices.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.repositories.ClientRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.IOException;
import java.util.*;
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

    public Optional<Client> findByName(@NotBlank String name) {
        return clientRepository.findByName(name);
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

    private static List<Client> readJsonData(String filePath) {
        List<Client> clients = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File path = new File(filePath);
            JsonNode jsonArrayNode = objectMapper.readTree(path);

            if (jsonArrayNode.isArray()) {
                ArrayNode arrayNode = (ArrayNode) jsonArrayNode;

                for (JsonNode clientNode : arrayNode) {
                    String name = clientNode.get("name").asText();
                    String phoneNumber = clientNode.get("phoneNumber").asText();
                    String neighborhood = clientNode.get("neighborhood").asText();
                    String address = clientNode.get("address").asText();
                    String reference = clientNode.get("reference").asText();

                    Client client = new Client();
                    client.setName(name);
                    client.setPhoneNumber(phoneNumber);
                    client.setNeighborhood(neighborhood);
                    client.setAddress(address);
                    client.setReference(reference);

                    clients.add(client);
                }
            } else {
                System.out.println("File does not contain a JSON array");
            }
        } catch (IOException e) {
            System.out.println("Error when reading JSON array: " + e.getMessage());
        }

        return clients;
    }

    public void exportDataToClient() throws IOException {
        clientRepository.saveAll(readJsonData("src/main/resources/entities/clients.json"));
    }
}