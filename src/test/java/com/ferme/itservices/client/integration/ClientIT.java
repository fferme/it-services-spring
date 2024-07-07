package com.ferme.itservices.client.integration;

import com.ferme.itservices.api.client.dtos.ClientDTO;
import com.ferme.itservices.client.utils.ClientConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static com.ferme.itservices.client.utils.ClientConstants.CLIENT_A_UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientIT {
	private final ClientConstants clientConstants = ClientConstants.getInstance();

	@Autowired
	private WebTestClient webTestClient;

	@AfterEach
	public void cleanup() {
		Mockito.clearAllCaches();
		Mockito.clearInvocations();
	}

	@Test
	public void createClient_WithValidData_ReturnsCreated() {
		final ClientDTO newClientDTO = clientConstants.NEW_CLIENT_DTO;

		webTestClient.post().uri("/api/clients").bodyValue(newClientDTO)
			.exchange().expectStatus().isCreated()
			.expectBody(ClientDTO.class)
			.value(clientDTO -> assertThat(clientDTO.name(), is(newClientDTO.name())))
			.value(clientDTO -> assertThat(clientDTO.phoneNumber(), is(newClientDTO.phoneNumber())))
			.value(clientDTO -> assertThat(clientDTO.neighborhood(), is(newClientDTO.neighborhood())))
			.value(clientDTO -> assertThat(clientDTO.address(), is(newClientDTO.address())))
			.value(clientDTO -> assertThat(clientDTO.reference(), is(newClientDTO.reference())));
	}

	@Test
	public void createClient_WithInvalidData_ReturnsUnprocessableEntity() {
		final ClientDTO invalidClientDTO = clientConstants.INVALID_CLIENT_DTO;

		webTestClient.post()
			.uri("/api/clients")
			.bodyValue(invalidClientDTO).exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void updateClient_WithValidData_ReturnsUpdatedClient() {
		final ClientDTO newClientDTO = clientConstants.NEW_CLIENT_DTO;

		webTestClient.put()
			.uri("/api/clients/" + CLIENT_A_UUID)
			.bodyValue(newClientDTO).exchange()
			.expectStatus().isEqualTo(HttpStatus.OK);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void getClient_WithExistingId_ReturnsClient() {
		final ClientDTO clientDTO = clientConstants.CLIENT_DTO;

		webTestClient.get().uri("/api/clients/" + clientConstants.CLIENT.getId())
			.exchange().expectStatus().isOk()
			.expectBody()
			.jsonPath("$.name").isEqualTo(clientDTO.name())
			.jsonPath("$.phoneNumber").isEqualTo(clientDTO.phoneNumber())
			.jsonPath("$.neighborhood").isEqualTo(clientDTO.neighborhood())
			.jsonPath("$.address").isEqualTo(clientDTO.address())
			.jsonPath("$.reference").isEqualTo(clientDTO.reference());
	}

	@Test
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void getClientByName_WithExistingName_ReturnsClient() {
		final ClientDTO clientDTO = clientConstants.CLIENT_DTO;

		webTestClient.get().uri("/api/clients/name/" + clientDTO.name())
			.exchange().expectStatus().isOk()
			.expectBody()
			.jsonPath("$.name").isEqualTo(clientDTO.name())
			.jsonPath("$.phoneNumber").isEqualTo(clientDTO.phoneNumber())
			.jsonPath("$.neighborhood").isEqualTo(clientDTO.neighborhood())
			.jsonPath("$.address").isEqualTo(clientDTO.address())
			.jsonPath("$.reference").isEqualTo(clientDTO.reference());
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void listClients_ReturnsAllClientsSortedByName() {
		final List<ClientDTO> clientsDTO = clientConstants.CLIENTS_DTO;

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

		List<ClientDTO> sortedExpectedClients = clientsDTO.stream()
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

		Assertions.assertThat(sortedActualClients).containsExactlyElementsOf(sortedExpectedClients);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void removeClient_ReturnsNoContent() {
		webTestClient.delete().uri("/api/clients/" + CLIENT_A_UUID)
			.exchange().expectStatus().isNoContent();
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void removeClients_ReturnsNoContent() {
		webTestClient.delete().uri("/api/clients")
			.exchange().expectStatus().isNoContent();
	}
}