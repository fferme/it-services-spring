package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Client;

public class ClientConstants {
	public static final Client VALID_CLIENT = Client.builder()
		.name("Ferme")
		.phoneNumber("21986861613")
		.neighborhood("Tijuca")
		.address("Rua Silva Perez 39/21")
		.reference("Amigo do Jaca")
		.build();

	public static final Client INVALID_CLIENT = new Client();
}