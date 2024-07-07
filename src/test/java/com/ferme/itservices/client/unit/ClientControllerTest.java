package com.ferme.itservices.client.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.api.client.controllers.ClientController;
import com.ferme.itservices.api.client.dtos.ClientDTO;
import com.ferme.itservices.api.client.models.Client;
import com.ferme.itservices.api.client.services.ClientService;
import com.ferme.itservices.client.utils.ClientConstants;
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

import static com.ferme.itservices.api.client.dtos.mappers.ClientMapper.toClientDTO;
import static com.ferme.itservices.client.utils.ClientConstants.CLIENT_A_UUID;
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
		final ClientDTO clientDTO = clientConstants.CLIENT_DTO;

		when(clientService.create(clientDTO)).thenReturn(clientDTO);

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(clientDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value(clientDTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(clientDTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(clientDTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(clientDTO.address()))
			.andExpect(jsonPath("$.reference").value(clientDTO.reference()));
	}

	@Test
	public void createClient_WithInvalidData_ReturnsUnprocessableEntity() throws Exception {
		final ClientDTO emptyClientDTO = clientConstants.EMPTY_CLIENT_DTO;
		final ClientDTO invalidClientDTO = clientConstants.INVALID_CLIENT_DTO;

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(emptyClientDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(invalidClientDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void createClient_WithExistingPhoneNumber_ReturnsConflict() throws Exception {
		final Client client = clientConstants.CLIENT;

		when(clientService.create(any(ClientDTO.class))).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(client))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void updateClient_WithValidDataAndId_ReturnsOk() throws Exception {
		final ClientDTO newClientDTO = clientConstants.NEW_CLIENT_DTO;

		when(clientService.update(CLIENT_A_UUID, newClientDTO)).thenReturn(newClientDTO);

		mockMvc
			.perform(
				put("/api/clients/" + CLIENT_A_UUID)
					.content(objectMapper.writeValueAsString(newClientDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(newClientDTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(newClientDTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(newClientDTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(newClientDTO.address()))
			.andExpect(jsonPath("$.reference").value(newClientDTO.reference()));
	}

	@Test
	public void updateClient_WithUnexistentId_ReturnsNotFound() throws Exception {
		final ClientDTO newClientDTO = clientConstants.NEW_CLIENT_DTO;

		when(clientService.update(eq(UUID.randomUUID()), any(ClientDTO.class))).thenReturn(null);

		mockMvc
			.perform(
				put("/api/clients/" + UUID.randomUUID())
					.content(objectMapper.writeValueAsString(newClientDTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() throws Exception {
		final Client client = clientConstants.CLIENT;
		final ClientDTO clientDTO = toClientDTO(client);

		when(clientService.findById(CLIENT_A_UUID)).thenReturn(clientDTO);

		mockMvc.perform(get("/api/clients/" + CLIENT_A_UUID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(clientDTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(clientDTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(clientDTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(clientDTO.address()))
			.andExpect(jsonPath("$.reference").value(clientDTO.reference()));
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() throws Exception {
		final Client client = clientConstants.CLIENT;
		final ClientDTO clientDTO = toClientDTO(client);

		when(clientService.findByName(clientDTO.name())).thenReturn((clientDTO));

		mockMvc.perform(get("/api/clients/name/" + clientDTO.name()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(clientDTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(clientDTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(clientDTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(clientDTO.address()))
			.andExpect(jsonPath("$.reference").value(clientDTO.reference()));
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/clients/name/Inexistente"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listClients_WhenClientsExists_ReturnsAllClientsSortedByName() throws Exception {
		final List<ClientDTO> clientsDTO = clientConstants.CLIENTS_DTO;

		when(clientService.listAll()).thenReturn(clientsDTO);

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