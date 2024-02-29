package com.ferme.itservices.api.services;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.dtos.mappers.ClientMapper;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public List<ClientDTO> listAll() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                      .map(clientMapper::toDTO)
                      .sorted(Comparator.comparing(ClientDTO::getName))
                      .collect(Collectors.toList());
    }

    public ClientDTO findById(@Valid @NotNull UUID id) {
        return clientRepository.findById(id).map(clientMapper::toDTO)
                               .orElseThrow(() -> new RecordNotFoundException(Client.class, id));
    }

    public ClientDTO create(@Valid @NotNull ClientDTO clientDTO) {
        return clientMapper.toDTO(clientRepository.save(clientMapper.toEntity(clientDTO)));
    }

    public ClientDTO update(@NotNull UUID id, @Valid @NotNull ClientDTO newClientDTO) {
        return clientRepository.findById(id)
                               .map(clientFound -> {
                                   clientFound.setNeighborhood(newClientDTO.getNeighborhood());
                                   clientFound.setAddress(newClientDTO.getAddress());
                                   clientFound.setReference(newClientDTO.getReference());

                                   return clientMapper.toDTO(clientRepository.save(clientFound));

                               }).orElseThrow(() -> new RecordNotFoundException(Client.class, id));
    }

    public void deleteById(@NotNull UUID id) {
        clientRepository.delete(clientRepository.findById(id)
                                                .orElseThrow(() -> new RecordNotFoundException(Client.class, id)));
    }

    public void deleteAll() {
        clientRepository.deleteAll();
    }

    public void exportDataToClient() {
        try {
            InputStream stream = new FileInputStream("src/main/resources/entities/clients.json");
            JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Gson gson = new Gson();

            reader.beginArray();
            while (reader.hasNext()) {
                ClientDTO clientDTO = clientMapper.toDTO(gson.fromJson(reader, Client.class));
                clientRepository.save(clientMapper.toEntity(clientDTO));
            }
            reader.endArray();
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}