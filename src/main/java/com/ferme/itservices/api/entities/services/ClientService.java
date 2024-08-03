package com.ferme.itservices.api.entities.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ferme.itservices.api.application.exceptions.RecordNotFoundException;
import com.ferme.itservices.api.application.services.GenericCRUDService;
import com.ferme.itservices.api.entities.dtos.ClientDTO;
import com.ferme.itservices.api.entities.dtos.mappers.ClientMapper;
import com.ferme.itservices.api.entities.models.Client;
import com.ferme.itservices.api.entities.repositories.ClientRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ferme.itservices.api.application.utils.JsonDataRead.readJsonData;

@Service
@AllArgsConstructor
public class ClientService implements GenericCRUDService<ClientDTO, UUID> {
	private final ClientRepository clientRepository;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Cacheable(value = "clientsList")
	public List<ClientDTO> listAll() {
		List<Client> clients = clientRepository.findAll();

		return ClientMapper.toClientDTOList(
			clients.stream()
				.sorted(Comparator.comparing(Client::getName))
				.collect(Collectors.toList())
		);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Cacheable(value = "client", key = "#id")
	public ClientDTO findById(@Valid @NotNull UUID id) {
		Client client = clientRepository.findById(id)
			.orElseThrow(() -> new RecordNotFoundException(Client.class, id.toString()));

		return ClientMapper.toClientDTO(client);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Cacheable(value = "client", key = "#name")
	public ClientDTO findByName(@NotBlank String name) {
		Client client = clientRepository.findByName(name)
			.orElseThrow(() -> new RecordNotFoundException(Client.class, name));

		return ClientMapper.toClientDTO(client);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Cacheable(value = "client", key = "{#name, #phoneNumber}")
	public ClientDTO findByNameAndPhoneNumber(@NotBlank String name, @NotBlank String phoneNumber) {
		Client client = clientRepository.findByNameAndPhoneNumber(name, phoneNumber)
			.orElseThrow(() -> new RecordNotFoundException(Client.class, name));

		return ClientMapper.toClientDTO(client);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "clientsList", allEntries = true)
	@CachePut(value = "client", key = "#result.id")
	public ClientDTO create(ClientDTO clientDTO) {
		return ClientMapper.toClientDTO(clientRepository.save(ClientMapper.toClient(clientDTO)));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "clientsList", allEntries = true)
	@CachePut(value = "client", key = "#id")
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = {"client", "clientsList"}, key = "#id")
	public void deleteById(@NotNull UUID id) {
		clientRepository.deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = {"client", "clientsList"}, allEntries = true)
	public void deleteAll() {
		clientRepository.deleteAll();
	}

	@Generated
	public List<ClientDTO> exportDataToClient() {
		List<Client> clients = clientRepository.saveAll(
			readJsonData("src/main/resources/entities/clients.json", new TypeReference<List<Client>>() { })
		);
		return ClientMapper.toClientDTOList(clients);
	}
}