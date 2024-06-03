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

import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTO;
import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTOList;
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
		final Client NEW_CLIENT = clientConstants.NEW_CLIENT;
		final ClientDTO NEW_CLIENT_DTO = toClientDTO(NEW_CLIENT);

		when(clientRepository.save(NEW_CLIENT)).thenReturn(NEW_CLIENT);

		ClientDTO sut = clientService.create(NEW_CLIENT_DTO);

		clientAssertions.assertClientProps(NEW_CLIENT_DTO, sut);
	}

	@Test
	public void createClient_WithInvalidData_ThrowsException() {
		final Client EMPTY_CLIENT = clientConstants.EMPTY_CLIENT;
		final ClientDTO EMPTY_CLIENT_DTO = toClientDTO(EMPTY_CLIENT);

		final Client INVALID_CLIENT = clientConstants.INVALID_CLIENT;
		final ClientDTO INVALID_CLIENT_DTO = toClientDTO(INVALID_CLIENT);

		when(clientRepository.save(EMPTY_CLIENT)).thenThrow(RuntimeException.class);
		when(clientRepository.save(INVALID_CLIENT)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> clientService.create(EMPTY_CLIENT_DTO)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> clientService.create(INVALID_CLIENT_DTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void updateClient_WithExistingClient_ReturnsUpdatedClient() {
		final Client CLIENT_A = clientConstants.CLIENT;

		final Client NEW_CLIENT = clientConstants.NEW_CLIENT;
		final ClientDTO NEW_CLIENT_DTO = toClientDTO(NEW_CLIENT);

		when(clientRepository.findById(CLIENT_A.getId())).thenReturn(Optional.of(CLIENT_A));
		when(clientRepository.save(CLIENT_A)).thenReturn(CLIENT_A);

		ClientDTO updatedClientDTO = clientService.update(CLIENT_A.getId(), NEW_CLIENT_DTO);

		assertEquals(NEW_CLIENT.getNeighborhood(), updatedClientDTO.neighborhood());
		assertEquals(NEW_CLIENT.getAddress(), updatedClientDTO.address());
		assertEquals(NEW_CLIENT.getReference(), updatedClientDTO.reference());
	}

	@Test
	public void updateClient_WithUnexistingClient_ThrowsRecordNotFoundException() {
		final Client NEW_CLIENT = clientConstants.NEW_CLIENT;
		final ClientDTO NEW_CLIENT_DTO = toClientDTO(NEW_CLIENT);

		assertThrows(RecordNotFoundException.class, () ->
			clientService.update(UUID.randomUUID(), NEW_CLIENT_DTO)
		);
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		final Client CLIENT_A = clientConstants.CLIENT;
		final ClientDTO CLIENT_A_DTO = toClientDTO(CLIENT_A);

		when(clientRepository.findById(CLIENT_A.getId())).thenReturn(of(CLIENT_A));

		ClientDTO sut = clientService.findById(CLIENT_A.getId());

		clientAssertions.assertClientProps(CLIENT_A_DTO, sut);
	}

	@Test
	public void getClient_ByUnexistingId_ThrowsRecordNotFoundException() {
		assertThatThrownBy(() -> clientService.findById(UUID.randomUUID())).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() {
		final Client CLIENT_A = clientConstants.CLIENT;
		final ClientDTO CLIENT_A_DTO = toClientDTO(CLIENT_A);

		when(clientRepository.findByName(CLIENT_A.getName())).thenReturn(of(CLIENT_A));

		ClientDTO sut = clientService.findByName(CLIENT_A.getName());

		clientAssertions.assertClientProps(CLIENT_A_DTO, sut);
	}

	@Test
	public void getClient_ByUnexistingName_ThrowsRecordNotFoundException() {
		assertThatThrownBy(() -> clientService.findByName("Unexisting Name")).isInstanceOf(RecordNotFoundException.class);
	}

	@Test
	public void listClients_WhenClientsExists_ReturnsAllClientsSortedByName() {
		final List<Client> CLIENTS = clientConstants.CLIENTS;
		final List<ClientDTO> CLIENTS_DTO = toClientDTOList(CLIENTS);

		when(clientRepository.findAll()).thenReturn(CLIENTS);

		List<ClientDTO> sut = clientService.listAll();

		clientAssertions.assertClientListProps(CLIENTS_DTO, sut);
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