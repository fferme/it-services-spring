package com.ferme.itservices.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.client.utils.ClientConstants;
import com.ferme.itservices.controllers.ClientController;
import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.models.Client;
import com.ferme.itservices.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.client.utils.ClientConstants.CLIENT_A_UUID;
import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTO;
import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTOList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
	private final ClientConstants clientConstants = ClientConstants.getInstance();

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ClientService clientService;

	@Test
	public void createClient_WithValidData_ReturnsCreated() throws Exception {
		final Client CLIENT_A = clientConstants.CLIENT;
		final ClientDTO CLIENT_A_DTO = toClientDTO(CLIENT_A);

		when(clientService.create(CLIENT_A_DTO)).thenReturn(CLIENT_A_DTO);

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(CLIENT_A_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value(CLIENT_A_DTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(CLIENT_A_DTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(CLIENT_A_DTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(CLIENT_A_DTO.address()))
			.andExpect(jsonPath("$.reference").value(CLIENT_A_DTO.reference()));
	}

	@Test
	public void createClient_WithInvalidData_ReturnsUnprocessableEntity() throws Exception {
		final Client EMPTY_CLIENT = clientConstants.EMPTY_CLIENT;
		final ClientDTO EMPTY_CLIENT_DTO = toClientDTO(EMPTY_CLIENT);

		final Client INVALID_CLIENT = clientConstants.INVALID_CLIENT;
		final ClientDTO INVALID_CLIENT_DTO = toClientDTO(INVALID_CLIENT);

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(EMPTY_CLIENT_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(INVALID_CLIENT_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void createClient_WithExistingPhoneNumber_ReturnsConflict() throws Exception {
		final Client CLIENT_A = clientConstants.CLIENT;

		when(clientService.create(any(ClientDTO.class))).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(CLIENT_A))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void updateClient_WithValidDataAndId_ReturnsOk() throws Exception {
		final Client NEW_CLIENT = clientConstants.NEW_CLIENT;
		final ClientDTO NEW_CLIENT_DTO = toClientDTO(NEW_CLIENT);

		when(clientService.update(CLIENT_A_UUID, NEW_CLIENT_DTO)).thenReturn(NEW_CLIENT_DTO);

		mockMvc
			.perform(
				put("/api/clients/" + CLIENT_A_UUID)
					.content(objectMapper.writeValueAsString(NEW_CLIENT_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(NEW_CLIENT_DTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(NEW_CLIENT_DTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(NEW_CLIENT_DTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(NEW_CLIENT_DTO.address()))
			.andExpect(jsonPath("$.reference").value(NEW_CLIENT_DTO.reference()));
	}

	@Test
	public void updateClient_WithUnexistentId_ReturnsNotFound() throws Exception {
		final ClientDTO NEW_CLIENT_DTO = toClientDTO(clientConstants.NEW_CLIENT);

		when(clientService.update(eq(UUID.randomUUID()), any(ClientDTO.class))).thenReturn(null);

		mockMvc
			.perform(
				put("/api/clients/" + UUID.randomUUID())
					.content(objectMapper.writeValueAsString(NEW_CLIENT_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() throws Exception {
		final Client CLIENT_A = clientConstants.CLIENT;
		final ClientDTO CLIENT_A_DTO = toClientDTO(CLIENT_A);

		when(clientService.findById(CLIENT_A_UUID)).thenReturn(CLIENT_A_DTO);

		mockMvc.perform(get("/api/clients/" + CLIENT_A_UUID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(CLIENT_A_DTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(CLIENT_A_DTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(CLIENT_A_DTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(CLIENT_A_DTO.address()))
			.andExpect(jsonPath("$.reference").value(CLIENT_A_DTO.reference()));
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() throws Exception {
		final Client CLIENT_A = clientConstants.CLIENT;
		final ClientDTO CLIENT_A_DTO = toClientDTO(CLIENT_A);

		when(clientService.findByName(CLIENT_A_DTO.name())).thenReturn((CLIENT_A_DTO));

		mockMvc.perform(get("/api/clients/name/" + CLIENT_A_DTO.name()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(CLIENT_A_DTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(CLIENT_A_DTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(CLIENT_A_DTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(CLIENT_A_DTO.address()))
			.andExpect(jsonPath("$.reference").value(CLIENT_A_DTO.reference()));
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/clients/name/Inexistente"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listClients_WhenClientsExists_ReturnsAllClientsSortedByName() throws Exception {
		final List<Client> CLIENTS = clientConstants.CLIENTS;
		final List<ClientDTO> CLIENTS_DTO = toClientDTOList(CLIENTS);

		when(clientService.listAll()).thenReturn(CLIENTS_DTO);

		mockMvc
			.perform(get("/api/clients"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void listClients_WhenClientsDoesNotExists_ReturnsEmptyList() throws Exception {
		when(clientService.listAll()).thenReturn(Collections.emptyList());

		mockMvc
			.perform(get("/api/clients"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void removeClient_WithExistingId_ReturnsNoContent() throws Exception {
		doNothing().when(clientService).deleteById(CLIENT_A_UUID);

		mockMvc
			.perform(delete("/api/clients/" + CLIENT_A_UUID))
			.andExpect(status().isNoContent());
	}

	@Test
	public void removeAllClients_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/clients"))
			.andExpect(status().isNoContent());
	}
}