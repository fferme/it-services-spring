package com.ferme.itservices.component.domain;

import com.ferme.itservices.models.Client;
import com.ferme.itservices.repositories.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.common.ClientConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class ClientRepositoryTest {
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@AfterEach
	public void nullifyId() {
		FELIPE.setId(null);
	}

	@Test
	public void createClient_WithValidData_ReturnsClient() {
		Client client = clientRepository.save(FELIPE);

		Client sut = testEntityManager.find(Client.class, client.getId());

		assertThat(sut).isNotNull();
		assertThat(sut.getName()).isEqualTo(FELIPE.getName());
		assertThat(sut.getPhoneNumber()).isEqualTo(FELIPE.getPhoneNumber());
		assertThat(sut.getNeighborhood()).isEqualTo(FELIPE.getNeighborhood());
		assertThat(sut.getAddress()).isEqualTo(FELIPE.getAddress());
		assertThat(sut.getReference()).isEqualTo(FELIPE.getReference());
	}

	@Test
	public void createClient_WithInvalidData_ThrowsException() {
		assertThatThrownBy(() -> clientRepository.save(EMPTY_CLIENT)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> clientRepository.save(INVALID_CLIENT)).isInstanceOf(RuntimeException.class);
	}


	@Test
	public void createClient_WithExistingPhoneNumber_ThrowsException() {
		Client client = testEntityManager.persistFlushFind(FELIPE);
		testEntityManager.detach(client);
		client.setId(null);

		assertThatThrownBy(() -> clientRepository.save(client)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		Client client = testEntityManager.persistFlushFind(FELIPE);

		Optional<Client> clientOpt = clientRepository.findById(client.getId());

		assertThat(clientOpt).isNotEmpty();
		assertThat(clientOpt.orElse(null)).isEqualTo(client);
	}

	@Test
	public void getClient_ByUnexistingId_ReturnsEmpty() {
		Optional<Client> clientOpt = clientRepository.findById(1L);

		assertThat(clientOpt).isEmpty();
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() {
		Client client = testEntityManager.persistFlushFind(FELIPE);

		Optional<Client> clientOpt = clientRepository.findByName(client.getName());

		assertThat(clientOpt).isNotEmpty();
		assertThat(clientOpt.orElse(null)).isEqualTo(client);
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsEmpty() {
		Optional<Client> clientOpt = clientRepository.findByName("Inexistente");

		assertThat(clientOpt).isEmpty();
	}

	@Sql(scripts = "/resources/scripts/import_clients.sql")
	@Test
	public void listClients_ReturnsClients() {
		List<Client> clients = clientRepository.findAll();

		assertThat(clients).isNotEmpty();
		assertThat(clients).hasSize(3);
	}

	@Test
	public void listClients_ReturnsNoClients() {
		List<Client> clients = clientRepository.findAll();

		assertThat(clients).isEmpty();
	}

	@Test
	public void removeClient_WithExistingId_RemovesClientFromDatabase() {
		Client client = testEntityManager.persistFlushFind(FELIPE);

		clientRepository.deleteById(FELIPE.getId());

		Client removedClient = testEntityManager.find(Client.class, FELIPE.getId());
		assertThat(removedClient).isNull();
	}
}