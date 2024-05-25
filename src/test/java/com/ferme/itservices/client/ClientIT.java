package com.ferme.itservices.client;

import com.ferme.itservices.dtos.ClientDTO;
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
import java.util.stream.Collectors;

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
		webTestClient.post().uri("/api/clients").bodyValue(NEW_CLIENT_DTO)
			.exchange().expectStatus().isCreated()
			.expectBody()
			.jsonPath("$.name").isEqualTo(NEW_CLIENT_DTO.name())
			.jsonPath("$.phoneNumber").isEqualTo(NEW_CLIENT_DTO.phoneNumber())
			.jsonPath("$.neighborhood").isEqualTo(NEW_CLIENT_DTO.neighborhood())
			.jsonPath("$.address").isEqualTo(NEW_CLIENT_DTO.address())
			.jsonPath("$.reference").isEqualTo(NEW_CLIENT_DTO.reference());
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
			.uri("/api/clients/" + CLIENT_A_UUID)
			.bodyValue(NEW_CLIENT).exchange()
			.expectStatus().isEqualTo(HttpStatus.OK);
	}

	@Test
	public void getClient_WithExistingId_ReturnsClient() {
		webTestClient.get().uri("/api/clients/" + CLIENT_A_UUID)
			.exchange().expectStatus().isOk()
			.expectBody()
			.jsonPath("$.name").isEqualTo(CLIENT_A_DTO.name())
			.jsonPath("$.phoneNumber").isEqualTo(CLIENT_A_DTO.phoneNumber())
			.jsonPath("$.neighborhood").isEqualTo(CLIENT_A_DTO.neighborhood())
			.jsonPath("$.address").isEqualTo(CLIENT_A_DTO.address())
			.jsonPath("$.reference").isEqualTo(CLIENT_A_DTO.reference());
	}

	@Test
	public void getClientByName_WithExistingName_ReturnsClient() {
		webTestClient.get().uri("/api/clients/name/" + CLIENT_A_DTO.name())
			.exchange().expectStatus().isOk()
			.expectBody()
			.jsonPath("$.name").isEqualTo(CLIENT_A_DTO.name())
			.jsonPath("$.phoneNumber").isEqualTo(CLIENT_A_DTO.phoneNumber())
			.jsonPath("$.neighborhood").isEqualTo(CLIENT_A_DTO.neighborhood())
			.jsonPath("$.address").isEqualTo(CLIENT_A_DTO.address())
			.jsonPath("$.reference").isEqualTo(CLIENT_A_DTO.reference());
	}

	@Test
	public void listClients_ReturnsAllClientsSortedByName() {
		List<ClientDTO> actualClients = webTestClient.get().uri("/api/clients")
			.accept(MediaType.APPLICATION_JSON).exchange()
			.expectStatus().isOk()
			.expectBodyList(ClientDTO.class)
			.returnResult().getResponseBody();

		assert actualClients != null;
		List<ClientDTO> sortedActualClients = actualClients.stream()
			.sorted(Comparator.comparing(ClientDTO::name))
			.map(clientDTO -> ClientDTO.builder()
				.name(clientDTO.name())
				.phoneNumber(clientDTO.phoneNumber())
				.neighborhood(clientDTO.neighborhood())
				.address(clientDTO.address())
				.reference(clientDTO.reference())
				.build()
			)
			.collect(Collectors.toList());

		List<ClientDTO> sortedExpectedClients = CLIENTS_DTO.stream()
			.sorted(Comparator.comparing(ClientDTO::name))
			.map(clientDTO -> ClientDTO.builder()
				.name(clientDTO.name())
				.phoneNumber(clientDTO.phoneNumber())
				.neighborhood(clientDTO.neighborhood())
				.address(clientDTO.address())
				.reference(clientDTO.reference())
				.build()
			)
			.collect(Collectors.toList());

		assertThat(sortedActualClients).containsExactlyElementsOf(sortedExpectedClients);
	}

	@Test
	public void removeClient_ReturnsNoContent() {
		webTestClient.delete().uri("/api/clients/" + CLIENT_A_UUID)
			.exchange().expectStatus().isNoContent();
	}

	@Test
	public void removeClients_ReturnsNoContent() {
		webTestClient.delete().uri("/api/clients")
			.exchange().expectStatus().isNoContent();
	}
}