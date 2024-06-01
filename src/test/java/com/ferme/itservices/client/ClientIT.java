package com.ferme.itservices.client;

import com.ferme.itservices.client.utils.ClientConstants;
import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.models.Client;
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
import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTO;
import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTOList;
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
		final Client NEW_CLIENT = clientConstants.NEW_CLIENT;
		final ClientDTO NEW_CLIENT_DTO = toClientDTO(NEW_CLIENT);

		webTestClient.post().uri("/api/clients").bodyValue(NEW_CLIENT_DTO)
			.exchange().expectStatus().isCreated()
			.expectBody(ClientDTO.class)
			.value(clientDTO -> assertThat(clientDTO.name(), is(NEW_CLIENT_DTO.name())))
			.value(clientDTO -> assertThat(clientDTO.phoneNumber(), is(NEW_CLIENT_DTO.phoneNumber())))
			.value(clientDTO -> assertThat(clientDTO.neighborhood(), is(NEW_CLIENT_DTO.neighborhood())))
			.value(clientDTO -> assertThat(clientDTO.address(), is(NEW_CLIENT_DTO.address())))
			.value(clientDTO -> assertThat(clientDTO.reference(), is(NEW_CLIENT_DTO.reference())));
	}

	@Test
	public void createClient_WithInvalidData_ReturnsUnprocessableEntity() {
		final Client INVALID_CLIENT = clientConstants.INVALID_CLIENT;

		webTestClient.post()
			.uri("/api/clients")
			.bodyValue(INVALID_CLIENT).exchange()
			.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void updateClient_WithValidData_ReturnsUpdatedClient() {
		final Client NEW_CLIENT = clientConstants.NEW_CLIENT;

		webTestClient.put()
			.uri("/api/clients/" + CLIENT_A_UUID)
			.bodyValue(NEW_CLIENT).exchange()
			.expectStatus().isEqualTo(HttpStatus.OK);
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void getClient_WithExistingId_ReturnsClient() {
		final Client CLIENT_A = clientConstants.CLIENT;
		final Client NEW_CLIENT = clientConstants.NEW_CLIENT;

		webTestClient.get().uri("/api/clients/" + clientConstants.CLIENT.getId())
			.exchange().expectStatus().isOk()
			.expectBody()
			.jsonPath("$.name").isEqualTo(clientConstants.CLIENT_A_DTO.name())
			.jsonPath("$.phoneNumber").isEqualTo(clientConstants.CLIENT_A_DTO.phoneNumber())
			.jsonPath("$.neighborhood").isEqualTo(clientConstants.CLIENT_A_DTO.neighborhood())
			.jsonPath("$.address").isEqualTo(clientConstants.CLIENT_A_DTO.address())
			.jsonPath("$.reference").isEqualTo(clientConstants.CLIENT_A_DTO.reference());
	}

	@Test
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void getClientByName_WithExistingName_ReturnsClient() {
		webTestClient.get().uri("/api/clients/name/" + clientConstants.CLIENT_A_DTO.name())
			.exchange().expectStatus().isOk()
			.expectBody()
			.jsonPath("$.name").isEqualTo(clientConstants.CLIENT_A_DTO.name())
			.jsonPath("$.phoneNumber").isEqualTo(clientConstants.CLIENT_A_DTO.phoneNumber())
			.jsonPath("$.neighborhood").isEqualTo(clientConstants.CLIENT_A_DTO.neighborhood())
			.jsonPath("$.address").isEqualTo(clientConstants.CLIENT_A_DTO.address())
			.jsonPath("$.reference").isEqualTo(clientConstants.CLIENT_A_DTO.reference());
	}

	@Test
	@Sql(scripts = {"/scripts/truncate_tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/scripts/import_clients.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void listClients_ReturnsAllClientsSortedByName() {
		final List<Client> CLIENTS = clientConstants.CLIENTS;
		final List<ClientDTO> CLIENTS_DTO = toClientDTOList(CLIENTS);

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