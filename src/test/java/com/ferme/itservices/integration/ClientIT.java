package com.ferme.itservices.integration;

import com.ferme.itservices.models.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static com.ferme.itservices.common.ClientConstants.FELIPE_WITH_ID;
import static com.ferme.itservices.common.ClientConstants.JOAO;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void createClient_ReturnsCreated() {
		ResponseEntity<Client> sut = restTemplate.postForEntity("/api/clients", JOAO, Client.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(Objects.requireNonNull(sut.getBody()).getId()).isNotNull();
		assertThat(sut.getBody().getName()).isEqualTo(JOAO.getName());
		assertThat(sut.getBody().getPhoneNumber()).isEqualTo(JOAO.getPhoneNumber());
		assertThat(sut.getBody().getNeighborhood()).isEqualTo(JOAO.getNeighborhood());
		assertThat(sut.getBody().getAddress()).isEqualTo(JOAO.getAddress());
		assertThat(sut.getBody().getReference()).isEqualTo(JOAO.getReference());
		assertThat(sut.getBody().getOrders()).isEqualTo(JOAO.getOrders());
	}

	@Test
	public void getClient_ReturnsClient() {
		ResponseEntity<Client> sut = restTemplate.getForEntity("/api/clients/1", Client.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).isEqualTo(FELIPE_WITH_ID);
	}

}