package com.ferme.itservices.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.repositories.ClientRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ferme.itservices.dtos.mappers.ClientMapper.*;

@Service
@AllArgsConstructor
public class ClientService {
	private final ClientRepository clientRepository;

	public List<ClientDTO> listAll() {
		List<Client> clients = clientRepository.findAll();

		return toClientDTOList(
			clients.stream()
			.sorted(Comparator.comparing(Client::getName))
				.collect(Collectors.toList())
		);
	}

	public ClientDTO findById(@Valid @NotNull UUID id) {
		Client client = clientRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(Client.class, id.toString()));

		return toClientDTO(client);
	}

	public ClientDTO findByName(@NotBlank String name) {
		Client client = clientRepository.findByName(name)
			.orElseThrow(() -> new RecordNotFoundException(Client.class, name));

		return toClientDTO(client);
	}

	public ClientDTO create(ClientDTO clientDTO) {
		return toClientDTO(clientRepository.save(toClient(clientDTO)));
	}

	public ClientDTO update(@NotNull UUID id, @Valid @NotNull ClientDTO newClientDTO) {
		return clientRepository.findById(id)
			.map(clientFound -> {
				clientFound.setName(newClientDTO.name());
				clientFound.setNeighborhood(newClientDTO.neighborhood());
				clientFound.setAddress(newClientDTO.address());
				clientFound.setReference(newClientDTO.reference());

				return toClientDTO(clientRepository.save(clientFound));

			}).orElseThrow(() -> new RecordNotFoundException(Client.class, id.toString()));
	}

	public void deleteById(@NotNull UUID id) {
		clientRepository.deleteById(id);
	}

	public void deleteAll() {
		clientRepository.deleteAll();
	}

	@Generated
	public List<ClientDTO> exportDataToClient() throws IOException {
		return toClientDTOList(clientRepository.saveAll(readJsonData()));
	}

	@Generated
	private static List<Client> readJsonData() {
		List<Client> clients = new ArrayList<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			File path = new File("src/main/resources/entities/clients.json");
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
}