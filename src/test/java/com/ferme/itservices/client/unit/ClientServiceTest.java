package com.ferme.itservices.client.unit;

import com.ferme.itservices.client.utils.ClientAssertions;
import com.ferme.itservices.client.utils.ClientConstants;
import com.ferme.itservices.dtos.ClientDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ferme.itservices.client.utils.ClientConstants.CLIENT_A_UUID;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
	private final ClientConstants clientConstants = ClientConstants.getInstance();
	private final ClientAssertions clientAssertions = ClientAssertions.getInstance();

	@InjectMocks
	private ClientService clientService;

	@Mock
	private ClientRepository clientRepository;

	@Test
	public void createClient_WithValidData_ReturnsClient() {
		final Client newClient = Client.builder() // This variable is coming dirty
			.name("New Name")
			.phoneNumber("21989653626")
			.neighborhood("New Neighborhood")
			.address("New Address")
			.reference("New Reference")
			.build();
		final ClientDTO newClientDTO = clientConstants.NEW_CLIENT_DTO;

		when(clientRepository.save(newClient)).thenReturn(newClient);

		final ClientDTO sut = clientService.create(newClientDTO);

		clientAssertions.assertClientProps(newClientDTO, sut);
	}

	@Test
	public void createClient_WithInvalidData_ThrowsException() {
		final Client emptyClient = clientConstants.EMPTY_CLIENT;
		final ClientDTO emptyClientDTO = clientConstants.EMPTY_CLIENT_DTO;

		final Client invalidClient = clientConstants.INVALID_CLIENT;
		final ClientDTO invalidClientDTO = clientConstants.INVALID_CLIENT_DTO;

		when(clientRepository.save(emptyClient)).thenThrow(RuntimeException.class);
		when(clientRepository.save(invalidClient)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> clientService.create(emptyClientDTO)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> clientService.create(invalidClientDTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateClient_WithExistingClient_ReturnsUpdatedClient() {
		final Client client = clientConstants.CLIENT;
		final Client newClient = clientConstants.NEW_CLIENT;
		final ClientDTO newClientDTO = clientConstants.NEW_CLIENT_DTO;

		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		when(clientRepository.save(client)).thenReturn(client);

		final ClientDTO updatedClientDTO = clientService.update(client.getId(), newClientDTO);

		assertEquals(newClient.getNeighborhood(), updatedClientDTO.neighborhood());
		assertEquals(newClient.getAddress(), updatedClientDTO.address());
		assertEquals(newClient.getReference(), updatedClientDTO.reference());
	}

	@Test
	public void updateClient_WithUnexistingClient_ThrowsRecordNotFoundException() {
		final ClientDTO newClientDTO = clientConstants.NEW_CLIENT_DTO;

		assertThrows(RecordNotFoundException.class, () ->
			clientService.update(UUID.randomUUID(), newClientDTO)
		);
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		final Client client = clientConstants.CLIENT;
		final ClientDTO clientDTO = clientConstants.CLIENT_DTO;

		when(clientRepository.findById(client.getId())).thenReturn(of(client));

		final ClientDTO sut = clientService.findById(client.getId());

		clientAssertions.assertClientProps(clientDTO, sut);
	}

	@Test
	public void getClient_ByUnexistingId_ThrowsRecordNotFoundException() {
		assertThatThrownBy(() -> clientService.findById(UUID.randomUUID())).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() {
		//final Client client = clientConstants.CLIENT; // This variable is coming dirty
		final Client client = new Client(
			CLIENT_A_UUID,
			"Felipe",
			"21986861613",
			"Tijuca",
			"Rua Silva Perez 39/21",
			null,
			"Amigo do Jaca"
		);
		final ClientDTO clientDTO = clientConstants.CLIENT_DTO;

		when(clientRepository.findByName(client.getName())).thenReturn(of(client));

		final ClientDTO sut = clientService.findByName(client.getName());

		clientAssertions.assertClientProps(clientDTO, sut);
	}

	@Test
	public void getClient_ByUnexistingName_ThrowsRecordNotFoundException() {
		assertThatThrownBy(() -> clientService.findByName("Unexisting Name")).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void listClients_WhenClientsExists_ReturnsAllClientsSortedByName() {
		final List<Client> clients = clientConstants.CLIENTS;
		final List<ClientDTO> clientsDTO = clientConstants.CLIENTS_DTO;

		when(clientRepository.findAll()).thenReturn(clients);

		final List<ClientDTO> sut = clientService.listAll();

		clientAssertions.assertClientListProps(clientsDTO, sut);
	}

	@Test
	public void listClients_WhenClientsDoesNotExists_ReturnsEmptyList() {
		when(clientRepository.findAll()).thenReturn(new ArrayList<>());

		final List<ClientDTO> sut = clientService.listAll();

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