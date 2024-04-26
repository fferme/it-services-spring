package com.ferme.itservices.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.controllers.ClientController;
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
import java.util.Optional;

import static com.ferme.itservices.common.ClientConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
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
		when(clientService.create(FELIPE)).thenReturn(FELIPE);

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(FELIPE))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$").value(FELIPE));
	}

	@Test
	public void createClient_WithInvalidData_ReturnsBadRequest() throws Exception {
		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(EMPTY_CLIENT))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(INVALID_CLIENT))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void createClient_WithExistingPhoneNumber_ReturnsConflict() throws Exception {
		when(clientService.create(any())).thenThrow(DataIntegrityViolationException.class);

		mockMvc
			.perform(
				post("/api/clients").content(objectMapper.writeValueAsString(FELIPE))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}

	@Test
	public void getClient_ByExistingId_ReturnsClient() throws Exception {
		when(clientService.findById(1L)).thenReturn(Optional.ofNullable(FELIPE));

		mockMvc.perform(get("/api/clients/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(FELIPE));
	}

	@Test
	public void getClient_ByUnexistingId_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/clients/1"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void getClient_ByExistingName_ReturnsClient() throws Exception {
		when(clientService.findByName(FELIPE.getName())).thenReturn(Optional.of(FELIPE));

		mockMvc.perform(get("/api/clients/name/" + FELIPE.getName()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(FELIPE));
	}

	@Test
	public void getClient_ByUnexistingName_ReturnsNotFound() throws Exception {
		mockMvc.perform(get("/api/clients/name/Inexistente"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void listClients_ReturnsClients() throws Exception {
		when(clientService.listAll()).thenReturn(CLIENTS);

		mockMvc
			.perform(get("/api/clients"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void listClients_ReturnsNoClients() throws Exception {
		when(clientService.listAll()).thenReturn(Collections.emptyList());

		mockMvc
			.perform(get("/api/clients"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void removeClient_WithExistingId_ReturnsNoContent() throws Exception {
		mockMvc
			.perform(delete("/api/clients/1"))
			.andExpect(status().isNoContent());
	}

	@Test
	public void removeClient_WithUnexistingId_ReturnsNotFound() throws Exception {
		doThrow(new EmptyResultDataAccessException(1)).when(clientService).deleteById(1L);

		mockMvc
			.perform(delete("/api/clients/1"))
			.andExpect(status().isNotFound());
	}
}