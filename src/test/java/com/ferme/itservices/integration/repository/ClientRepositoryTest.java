package com.ferme.itservices.integration.repository;

import com.ferme.itservices.models.Client;
import com.ferme.itservices.repositories.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
		VALID_CLIENT.setId(null);
	}

	@Test
	public void createClient_WithValidData_ReturnsClient() {
		Client client = clientRepository.save(VALID_CLIENT);

		Client sut = testEntityManager.find(Client.class, client.getId());

		assertThat(sut).isNotNull();
		assertThat(sut.getName()).isEqualTo(VALID_CLIENT.getName());
		assertThat(sut.getPhoneNumber()).isEqualTo(VALID_CLIENT.getPhoneNumber());
		assertThat(sut.getNeighborhood()).isEqualTo(VALID_CLIENT.getNeighborhood());
		assertThat(sut.getAddress()).isEqualTo(VALID_CLIENT.getAddress());
		assertThat(sut.getReference()).isEqualTo(VALID_CLIENT.getReference());
	}

	@Test
	public void createClient_WithInvalidData_ThrowsException() {
		assertThatThrownBy(() -> clientRepository.save(EMPTY_CLIENT)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> clientRepository.save(INVALID_CLIENT)).isInstanceOf(RuntimeException.class);
	}


	@Test
	public void createClient_WithExistingPhoneNumber_ThrowsException() {
		Client client = testEntityManager.persistFlushFind(VALID_CLIENT);
		testEntityManager.detach(client);
		client.setId(null);

		assertThatThrownBy(() -> clientRepository.save(client)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() {
		Client client = testEntityManager.persistFlushFind(VALID_CLIENT);

		Optional<Client> clientOpt = clientRepository.findById(client.getId());

		assertThat(clientOpt).isNotEmpty();
		assertThat(clientOpt.orElse(null)).isEqualTo(client);
	}

	@Test
	public void getClient_ByUnexistingId_ReturnsEmpty() {
		Optional<Client> clientOpt = clientRepository.findById(1L);

		assertThat(clientOpt).isEmpty();
	}
}