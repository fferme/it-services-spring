package com.ferme.itservices.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.controllers.ClientController;
import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static com.ferme.itservices.client.ClientConstants.*;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ClientService clientService;

	@Test
	public void createClient_WithValidData_ReturnsCreated() throws Exception {
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
			.andExpect(jsonPath("$.reference").value(CLIENT_A_DTO.reference()))
			.andExpect(jsonPath("$.orders").value(CLIENT_A_DTO.orders()));
	}

	@Test
	public void createClient_WithInvalidData_ReturnsUnprocessableEntity() throws Exception {
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
		when(clientService.create(any(ClientDTO.class))).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(CLIENT_A))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void updateClient_WithValidDataAndId_ReturnsOk() throws Exception {
		when(clientService.update(eq(CLIENT_A_UUID), any(ClientDTO.class))).thenReturn(NEW_CLIENT_DTO);

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
			.andExpect(jsonPath("$.reference").value(NEW_CLIENT_DTO.reference()))
			.andExpect(jsonPath("$.orders").value(NEW_CLIENT_DTO.orders()));
	}

	@Test
	public void updateClient_WithUnexistentId_ReturnsNotFound() throws Exception {
		when(clientService.update(eq(UUID.randomUUID()), any())).thenReturn(null);

		mockMvc
			.perform(
				put("/api/clients/" + UUID.randomUUID())
					.content(objectMapper.writeValueAsString(NEW_CLIENT_DTO))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() throws Exception {
		when(clientService.findById(any(UUID.class))).thenReturn((CLIENT_A_DTO));

		mockMvc.perform(get("/api/clients/" + UUID.randomUUID()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(CLIENT_A_DTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(CLIENT_A_DTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(CLIENT_A_DTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(CLIENT_A_DTO.address()))
			.andExpect(jsonPath("$.reference").value(CLIENT_A_DTO.reference()))
			.andExpect(jsonPath("$.orders").value(CLIENT_A_DTO.orders()));
	}

	@Test
	public void getClient_ByUnexistingId_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/clients/" + UUID.randomUUID()))
			.andExpect(status().isNotFound());
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() throws Exception {
		when(clientService.findByName(CLIENT_A_DTO.name())).thenReturn((CLIENT_A_DTO));

		mockMvc.perform(get("/api/clients/name/" + CLIENT_A_DTO.name()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(CLIENT_A_DTO.name()))
			.andExpect(jsonPath("$.phoneNumber").value(CLIENT_A_DTO.phoneNumber()))
			.andExpect(jsonPath("$.neighborhood").value(CLIENT_A_DTO.neighborhood()))
			.andExpect(jsonPath("$.address").value(CLIENT_A_DTO.address()))
			.andExpect(jsonPath("$.reference").value(CLIENT_A_DTO.reference()))
			.andExpect(jsonPath("$.orders").value(CLIENT_A_DTO.orders()));
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/clients/name/Inexistente"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listClients_WhenClientsExists_ReturnsAllClientsSortedByName() throws Exception {
		when(clientService.listAll()).thenReturn(CLIENTS_DTO);

		mockMvc
			.perform(get("/api/clients"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[*].name", containsInRelativeOrder(
				CLIENT_A_DTO.name(),
				CLIENT_B_DTO.name(),
				CLIENT_C_DTO.name()
			)));
		;
	}

	@Test
	public void listClients_WhenClientsDoesNotExists_ReturnsAllClientsSortedByName() throws Exception {
		when(clientService.listAll()).thenReturn(Collections.emptyList());

		mockMvc
			.perform(get("/api/clients"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void removeClient_WithExistingId_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/clients/" + UUID.randomUUID()))
			.andExpect(status().isNoContent());
	}

	@Test
	public void removeClient_WithUnexistingId_ReturnsNotFound() throws Exception {
		doThrow(new EmptyResultDataAccessException(1)).when(clientService).deleteById(CLIENT_A_UUID);

		mockMvc.perform(delete("/api/clients/" + CLIENT_A_UUID))
			.andExpect(status().isNotFound());
	}

	@Test
	public void removeAllClients_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/clients"))
			.andExpect(status().isNoContent());
	}
}