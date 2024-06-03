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
		Client client = clientRepository.save(newClient);

		Client sut = testEntityManager.find(Client.class, client.getId());

		assertThat(sut).isNotNull();
		assertThat(sut).isEqualTo(client);
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		Client client = testEntityManager.persistFlushFind(newClient);

		Optional<Client> clientOpt = clientRepository.findById(client.getId());

		assertThat(clientOpt).isNotEmpty();
		assertThat(clientOpt.orElse(null)).isEqualTo(client);
	}

	@Test
	public void getClient_ByUnexistingId_ReturnsEmpty() {
		Optional<Client> clientOpt = clientRepository.findById(UUID.randomUUID());

		assertThat(clientOpt).isEmpty();
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() {
		Client clientGet = testEntityManager.persistFlushFind(client);

		Optional<Client> clientOpt = clientRepository.findByName(clientGet.getName());

		assertThat(clientOpt).isNotEmpty();
		assertThat(clientOpt.orElse(null)).isEqualTo(clientGet);
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsEmpty() {
		Optional<Client> clientOpt = clientRepository.findByName("Inexistente");

		assertThat(clientOpt).isEmpty();
	}

	@Sql(scripts = "/scripts/import_clients.sql")
	@Test
	public void listClients_WhenClientsExists_ReturnsAllClients() {
		List<Client> clients = clientRepository.findAll();

		assertThat(clients).isNotEmpty();
		assertThat(clients).hasSize(3);
	}

	@Test
	public void listClients_WhenClientsDoesNotExist_ReturnsEmptyList() {
		List<Client> clients = clientRepository.findAll();

		assertThat(clients).isEmpty();
	}

	@Test
	public void deleteClient_WithExistingId_DeletesClientFromDatabase() {
		Client clientDel = testEntityManager.persistFlushFind(client);

		clientRepository.deleteById(client.getId());
		Client removedClient = testEntityManager.find(Client.class, clientDel.getId());

		assertThat(removedClient).isNull();
	}

	@Test
	public void deleteClient_WithNonExistingId_DoesNotDeleteAnything() {
		clientRepository.deleteById(UUID.randomUUID());

		Client nonExistingClient = testEntityManager.find(Client.class, UUID.randomUUID());
		assertThat(nonExistingClient).isNull();
	}

}