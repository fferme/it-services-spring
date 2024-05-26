package com.ferme.itservices.client;

import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import com.ferme.itservices.repositories.ClientRepository;
import com.ferme.itservices.services.ClientService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.ferme.itservices.client.ClientConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
	@InjectMocks
	private ClientService clientService;

	@Mock
	private ClientRepository clientRepository;

	@AfterAll
	public static void reset() {
		clearAllCaches();
	}

	@Test
	public void createClient_WithValidData_ReturnsClient() {
		when(clientRepository.save(CLIENT_A)).thenReturn(CLIENT_A);

		ClientDTO sut = clientService.create(CLIENT_A_DTO);

		assertThat(sut).isEqualTo(CLIENT_A_DTO);
	}

	@Test
	public void createClient_WithInvalidData_ThrowsException() {
		when(clientRepository.save(EMPTY_CLIENT)).thenThrow(RuntimeException.class);
		when(clientRepository.save(INVALID_CLIENT)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> clientService.create(EMPTY_CLIENT_DTO)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> clientService.create(INVALID_CLIENT_DTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateClient_WithExistingClient_ReturnsUpdatedClient() {
		when(clientRepository.findById(CLIENT_WITH_ID.getId())).thenReturn(Optional.of(CLIENT_WITH_ID));
		when(clientRepository.save(CLIENT_WITH_ID)).thenReturn(CLIENT_WITH_ID);

		ClientDTO updatedClientDTO = clientService.update(CLIENT_WITH_ID.getId(), NEW_CLIENT_DTO);

		assertEquals(NEW_CLIENT.getNeighborhood(), updatedClientDTO.neighborhood());
		assertEquals(NEW_CLIENT.getAddress(), updatedClientDTO.address());
		assertEquals(NEW_CLIENT.getReference(), updatedClientDTO.reference());
	}

	@Test
	public void updateClient_WithUnexistingClient_ThrowsRecordNotFoundException() {
		assertThrows(RecordNotFoundException.class, () ->
			clientService.update(UUID.randomUUID(), NEW_CLIENT_DTO)
		);
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		when(clientRepository.findById(CLIENT_A_UUID)).thenReturn(Optional.of(CLIENT_A));

		ClientDTO sut = clientService.findById(CLIENT_A_UUID);

		assertThat(sut).isNotNull();
		assertThat(sut).isEqualTo(CLIENT_A_DTO);
	}

	@Test
	public void getClient_ByUnexistingId_ThrowsRecordNotFoundException() {
		assertThatThrownBy(() -> clientService.findById(UUID.randomUUID())).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() {
		when(clientRepository.findByName(CLIENT_A.getName())).thenReturn(Optional.of(CLIENT_A));

		ClientDTO sut = clientService.findByName(CLIENT_A.getName());

		assertThat(sut).isNotNull();
		assertThat(sut).isEqualTo(CLIENT_A_DTO);
	}

	@Test
	public void getClient_ByUnexistingName_ThrowsRecordNotFoundException() {
		assertThatThrownBy(() -> clientService.findByName("Unexisting Name")).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void listClients_WhenClientsExists_ReturnsAllClientsSortedByName() {
		when(clientRepository.findAll()).thenReturn(CLIENTS);

		List<ClientDTO> sut = clientService.listAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(CLIENTS.size());
		assertThat(sut.get(0)).isEqualTo(CLIENT_A_DTO);
		assertThat(sut.get(1)).isEqualTo(CLIENT_B_DTO);
		assertThat(sut.get(2)).isEqualTo(CLIENT_C_DTO);
		assertThat(sut).isSortedAccordingTo(Comparator.comparing(ClientDTO::name));
	}

	@Test
	public void listClients_WhenClientsDoesNotExists_ReturnsEmptyList() {
		when(clientRepository.findAll()).thenReturn(new ArrayList<>());

		List<ClientDTO> sut = clientService.listAll();

		assertThat(sut).isEmpty();
	}

	@Test
	public void deleteClient_WithExistingId_doesNotThrowAnyException() {
		assertThatCode(() -> clientService.deleteById(UUID.randomUUID())).doesNotThrowAnyException();
	}

	@Test
	public void deleteClient_WithUnexistingId_ThrowsException() {
		doThrow(new RuntimeException()).when(clientRepository).deleteById(UUID.randomUUID());

		assertThatThrownBy(() -> clientService.deleteById(UUID.randomUUID())).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void deleteAllClients_doesNotThrowAnyException() {
		assertThatCode(() -> clientService.deleteAll()).doesNotThrowAnyException();
	}
}