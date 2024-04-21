package com.ferme.itservices.common;

import com.ferme.itservices.models.Client;

public class ClientConstants {
	public static final Client VALID_CLIENT = Client.builder()
		.name("Ferme")
		.phoneNumber("21986861613")
		.neighborhood("Tijuca")
		.address("Rua Silva Perez 39/21")
		.reference("Amigo do Jaca")
		.build();

	public static final Client EMPTY_CLIENT = new Client();

	public static final Client INVALID_CLIENT = Client.builder()
		.name("")
		.phoneNumber("21986861613333")
		.neighborhood("")
		.address("")
		.reference("")
		.build();
}