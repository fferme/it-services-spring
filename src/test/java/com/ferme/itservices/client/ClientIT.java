package com.ferme.itservices.client;

import com.ferme.itservices.models.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Comparator;
import java.util.List;

import static com.ferme.itservices.client.ClientConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

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
	public void updateClient_WithValidData_ReturnsUpdatedClient() {
		webTestClient.put()
			.uri("/api/clients/1")
			.bodyValue(NEW_CLIENT).exchange()
			.expectStatus().isEqualTo(HttpStatus.OK);
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
	public void listClients_ReturnsAllClientsSortedByName() {
		List<Client> actualClients = webTestClient.get().uri("/api/clients")
			.accept(MediaType.APPLICATION_JSON).exchange()
			.expectStatus().isOk()
			.expectBodyList(Client.class)
			.returnResult().getResponseBody();

		List<Client> sortedExpectedClients = CLIENTS.stream()
			.sorted(Comparator.comparing(Client::getName))
			.toList();

		assert actualClients != null;
		List<Client> sortedActualClients = actualClients.stream()
			.sorted(Comparator.comparing(Client::getName))
			.toList();

		assertThat(sortedActualClients).containsExactlyElementsOf(sortedExpectedClients);
	}

	@Test
	public void removeClient_ReturnsNoContent() {
		webTestClient.delete().uri("/api/clients/" + FELIPE.getId())
			.exchange().expectStatus().isNoContent();
	}

	@Test
	public void removeClients_ReturnsNoContent() {
		webTestClient.delete().uri("/api/clients")
			.exchange().expectStatus().isNoContent();
	}
}