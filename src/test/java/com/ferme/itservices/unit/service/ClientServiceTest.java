package com.ferme.itservices.unit.service;

import com.ferme.itservices.models.Client;
import com.ferme.itservices.repositories.ClientRepository;
import com.ferme.itservices.services.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.common.ClientConstants.EMPTY_CLIENT;
import static com.ferme.itservices.common.ClientConstants.VALID_CLIENT;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
	@InjectMocks
	private ClientService clientService;

	@Mock
	private ClientRepository clientRepository;

	@Test
	public void createClient_WithValidData_ReturnsClient() {
		when(clientRepository.save(VALID_CLIENT)).thenReturn(VALID_CLIENT);

		Client sut = clientService.create(VALID_CLIENT);

		assertThat(sut).isEqualTo(VALID_CLIENT);
	}

	@Test
	public void createClient_WithInvalidData_ThrowsException() {
		when(clientRepository.save(EMPTY_CLIENT)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> clientService.create(EMPTY_CLIENT)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(VALID_CLIENT));

		Optional<Client> sut = clientService.findById(1L);

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(VALID_CLIENT);
	}

	@Test
	public void getClient_ByUnexistingId_ReturnsEmpty() {
		when(clientRepository.findById(1L)).thenReturn(Optional.empty());

		Optional<Client> sut = clientService.findById(1L);

		assertThat(sut).isEmpty();
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() {
		when(clientRepository.findByName(VALID_CLIENT.getName())).thenReturn(Optional.of(VALID_CLIENT));

		Optional<Client> sut = clientService.findByName(VALID_CLIENT.getName());

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(VALID_CLIENT);
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsClient() {
		when(clientRepository.findByName("Unexisting name")).thenReturn(Optional.empty());

		Optional<Client> sut = clientService.findByName("Unexisting name");

		assertThat(sut).isEmpty();
	}

	@Test
	public void listClients_ReturnsAllClients() {
		List<Client> clients = new ArrayList<>() {
			{ add(VALID_CLIENT); }
		};
		when(clientRepository.findAll()).thenReturn(clients);

		List<Client> sut = clientRepository.findAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(1);
		assertThat(sut.getFirst()).isEqualTo(VALID_CLIENT);
	}

	@Test
	public void listClients_ReturnsNoClients() {
		when(clientRepository.findAll()).thenReturn(Collections.emptyList());

		List<Client> sut = clientRepository.findAll();

		assertThat(sut).isEmpty();
	}

	@Test
	public void deleteClient_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> clientService.deleteById(1L)).doesNotThrowAnyException();
	}

	@Test
	public void deleteClient_WithUnexistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(clientRepository).deleteById(1L);

		assertThatThrownBy(() -> clientService.deleteById(1L)).isInstanceOf(RuntimeException.class);
	}


}