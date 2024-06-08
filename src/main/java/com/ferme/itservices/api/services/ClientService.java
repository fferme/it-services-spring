package com.ferme.itservices.api.services;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.dtos.mappers.ClientMapper;
import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.repositories.ClientRepository;
import com.ferme.itservices.api.utils.JsonDataRead;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientService {
	private final ClientRepository clientRepository;

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<ClientDTO> listAll() {
		List<Client> clients = clientRepository.findAll();

		return ClientMapper.toClientDTOList(
			clients.stream()
				.sorted(Comparator.comparing(Client::getName))
				.collect(Collectors.toList())
		);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ClientDTO findById(@Valid @NotNull UUID id) {
		Client client = clientRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(Client.class, id.toString()));

		return ClientMapper.toClientDTO(client);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ClientDTO findByName(@NotBlank String name) {
		Client client = clientRepository.findByName(name)
			.orElseThrow(() -> new RecordNotFoundException(Client.class, name));

		return ClientMapper.toClientDTO(client);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ClientDTO findByNameAndPhoneNumber(@NotBlank String name, @NotBlank String phoneNumber) {
		Client client = clientRepository.findByNameAndPhoneNumber(name, phoneNumber)
			.orElseThrow(() -> new RecordNotFoundException(Client.class, name));

		return ClientMapper.toClientDTO(client);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ClientDTO create(ClientDTO clientDTO) {
		return ClientMapper.toClientDTO(clientRepository.save(ClientMapper.toClient(clientDTO)));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ClientDTO update(@NotNull UUID id, @Valid @NotNull ClientDTO newClientDTO) {
		return clientRepository.findById(id)
			.map(clientFound -> {
				clientFound.setName(newClientDTO.name());
				clientFound.setPhoneNumber(newClientDTO.phoneNumber());
				clientFound.setNeighborhood(newClientDTO.neighborhood());
				clientFound.setAddress(newClientDTO.address());
				clientFound.setReference(newClientDTO.reference());

				return ClientMapper.toClientDTO(clientRepository.save(clientFound));

			}).orElseThrow(() -> new RecordNotFoundException(Client.class, id.toString()));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteById(@NotNull UUID id) {
		clientRepository.deleteById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAll() {
		clientRepository.deleteAll();
	}

	@Generated
	public List<ClientDTO> exportDataToClient() {
		return ClientMapper.toClientDTOList(clientRepository.saveAll(JsonDataRead.readClientsJsonData()));
	}
}