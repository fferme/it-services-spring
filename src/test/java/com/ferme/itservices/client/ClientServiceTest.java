package com.ferme.itservices.client;

import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.repositories.ClientRepository;
import com.ferme.itservices.services.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.client.ClientConstants.*;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
		when(clientRepository.save(FELIPE)).thenReturn(FELIPE);

		Client sut = clientService.create(FELIPE);

		assertThat(sut).isEqualTo(FELIPE);
	}

	@Test
	public void createClient_WithInvalidData_ThrowsException() {
		when(clientRepository.save(EMPTY_CLIENT)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> clientService.create(EMPTY_CLIENT)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateClient_WithExistingClient_ReturnsUpdatedClient() {
		when(clientRepository.findById(FELIPE.getId())).thenReturn(java.util.Optional.of(FELIPE));
		when(clientRepository.save(FELIPE)).thenReturn(FELIPE);

		Client updatedClient = clientService.update(FELIPE.getId(), NEW_CLIENT);

		assertEquals(NEW_CLIENT.getNeighborhood(), updatedClient.getNeighborhood());
		assertEquals(NEW_CLIENT.getAddress(), updatedClient.getAddress());
		assertEquals(NEW_CLIENT.getReference(), updatedClient.getReference());
	}

	@Test
	public void updateClient_WithUnexistingClient_ReturnsRecordNotFoundException() {
		when(clientRepository.findById(9L)).thenReturn(empty());

		assertThrows(RecordNotFoundException.class, () -> {
			clientService.update(9L, NEW_CLIENT);
		});
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		when(clientRepository.findById(1L)).thenReturn(Optional.of(FELIPE));

		Optional<Client> sut = clientService.findById(1L);

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(FELIPE);
	}

	@Test
	public void getClient_ByUnexistingId_ReturnsEmpty() {
		when(clientRepository.findById(1L)).thenReturn(empty());

		Optional<Client> sut = clientService.findById(1L);

		assertThat(sut).isEmpty();
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() {
		when(clientRepository.findByName(FELIPE.getName())).thenReturn(Optional.of(FELIPE));

		Optional<Client> sut = clientService.findByName(FELIPE.getName());

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(FELIPE);
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsClient() {
		when(clientRepository.findByName("Unexisting name")).thenReturn(empty());

		Optional<Client> sut = clientService.findByName("Unexisting name");

		assertThat(sut).isEmpty();
	}

	@Test
	public void listClients_WhenClientsExists_ReturnsAllClientsSortedByName() {
		when(clientRepository.findAll()).thenReturn(CLIENTS);

		List<Client> sut = clientService.listAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(CLIENTS.size());
		assertThat(sut.get(0)).isEqualTo(FELIPE);
		;
		assertThat(sut.get(1)).isEqualTo(JOAO);
		;
		assertThat(sut.get(2)).isEqualTo(RONALDO);
		assertThat(sut).isSortedAccordingTo(Comparator.comparing(Client::getName));
	}

	@Test
	public void listClients_WhenClientsDoesNotExist_ReturnsEmptyList() {
		when(clientRepository.findAll()).thenReturn(new ArrayList<>());

		List<Client> sut = clientService.listAll();

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

	@Test
	public void deleteAllClients_doesNotThrowAnyException() {
		assertThatCode(() -> clientService.deleteAll()).doesNotThrowAnyException();
	}
}