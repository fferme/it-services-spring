package com.ferme.itservices.client.unit;

import com.ferme.itservices.client.utils.ClientConstants;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class ClientRepositoryTest {
	private final ClientConstants clientConstants = ClientConstants.getInstance();

	private final Client client = clientConstants.CLIENT;
	private final Client newClient = clientConstants.NEW_CLIENT;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@BeforeEach
	public void cleanup() {
		client.setId(null);
		Mockito.clearAllCaches();
		Mockito.clearInvocations();
	}

	@Test
	public void createClient_WithValidData_ReturnsClient() {
		Client savedClient = clientRepository.save(newClient);

		Client sut = testEntityManager.find(Client.class, savedClient.getId());

		assertThat(sut).isNotNull();
		assertThat(sut).isEqualTo(savedClient);
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		final Client savedClient = testEntityManager.persistFlushFind(newClient);

		final Optional<Client> clientFound = clientRepository.findById(savedClient.getId());

		assertThat(clientFound).isNotEmpty();
		assertThat(clientFound.orElse(null)).isEqualTo(savedClient);
	}

	@Test
	public void getClient_ByUnexistingId_ReturnsEmpty() {
		final Optional<Client> clientFound = clientRepository.findById(UUID.randomUUID());

		assertThat(clientFound).isEmpty();
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() {
		final Client savedClient = testEntityManager.persistFlushFind(client);

		final Optional<Client> clientFound = clientRepository.findByName(savedClient.getName());

		assertThat(clientFound).isNotEmpty();
		assertThat(clientFound.orElse(null)).isEqualTo(savedClient);
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsEmpty() {
		final Optional<Client> clientFound = clientRepository.findByName("Inexistente");

		assertThat(clientFound).isEmpty();
	}

	@Sql(scripts = "/scripts/import_clients.sql")
	@Test
	public void listClients_WhenClientsExists_ReturnsAllClients() {
		final List<Client> clients = clientRepository.findAll();

		assertThat(clients).isNotEmpty();
		assertThat(clients).hasSize(3);
	}

	@Test
	public void listClients_WhenClientsDoesNotExist_ReturnsEmptyList() {
		final List<Client> clients = clientRepository.findAll();

		assertThat(clients).isEmpty();
	}

	@Test
	public void deleteClient_WithExistingId_DeletesClientFromDatabase() {
		final Client savedClient = testEntityManager.persistFlushFind(client);

		clientRepository.deleteById(savedClient.getId());
		final Client removedClient = testEntityManager.find(Client.class, savedClient.getId());

		assertThat(removedClient).isNull();
	}

	@Test
	public void deleteClient_WithNonExistingId_DoesNotDeleteAnything() {
		clientRepository.deleteById(UUID.randomUUID());

		final Client nonExistingClient = testEntityManager.find(Client.class, UUID.randomUUID());
		assertThat(nonExistingClient).isNull();
	}

}