package com.ferme.itservices.integration;

import com.ferme.itservices.models.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.ferme.itservices.common.ClientConstants.*;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void createClient_WithValidData_ReturnsCreated() {
		webTestClient.post().uri("/api/clients").bodyValue(FELIPE)
			.exchange().expectStatus().isCreated()
			.expectBody(Client.class).isEqualTo(FELIPE);
	}

	@Test
	public void createClient_WithInvalidData_ReturnsUnprocessableEntity() {
		webTestClient.post()
			.uri("/api/clients")
			.bodyValue(INVALID_CLIENT).exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	public void getClient_WithExistingId_ReturnsClient() {
		webTestClient.get().uri("/api/clients/" + FELIPE.getId())
			.exchange().expectStatus().isOk()
			.expectBody(Client.class).isEqualTo(FELIPE);
	}

	@Test
	public void getClientByName_WithExistingName_ReturnsClient() {
		webTestClient.get().uri("/api/clients/name/" + FELIPE.getName())
			.exchange().expectStatus().isOk()
			.expectBody(Client.class).isEqualTo(FELIPE);
	}

	@Test
	public void listClients_ReturnsAllClients() {
		webTestClient.get().uri("/api/clients")
			.accept(MediaType.APPLICATION_JSON).exchange()
			.expectStatus().isOk()
			.expectBodyList(Client.class).hasSize(3).contains(FELIPE, JOAO, RONALDO);
	}

	@Test
	public void removeClient_ReturnsNoContent() {
		webTestClient.delete().uri("/api/clients/" + FELIPE.getId())
			.exchange().expectStatus().isNoContent();
	}
}