package com.ferme.itservices.integration;

import com.ferme.itservices.models.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static com.ferme.itservices.common.ClientConstants.FELIPE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientIT {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void createClient_ReturnsCreated() {
		ResponseEntity<Client> sut = restTemplate.postForEntity("/api/clients", FELIPE, Client.class);

		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(Objects.requireNonNull(sut.getBody()).getId()).isNotNull();
		assertThat(sut.getBody().getName()).isEqualTo(FELIPE.getName());
		assertThat(sut.getBody().getPhoneNumber()).isEqualTo(FELIPE.getPhoneNumber());
		assertThat(sut.getBody().getNeighborhood()).isEqualTo(FELIPE.getNeighborhood());
		assertThat(sut.getBody().getAddress()).isEqualTo(FELIPE.getAddress());
		assertThat(sut.getBody().getReference()).isEqualTo(FELIPE.getReference());
		assertThat(sut.getBody().getOrders()).isEqualTo(FELIPE.getOrders());
	}

}