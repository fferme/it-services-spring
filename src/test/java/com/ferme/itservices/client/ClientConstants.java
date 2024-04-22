package com.ferme.itservices.client;

import com.ferme.itservices.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientConstants {
	public static final Client FELIPE = Client.builder()
		.name("Ferme")
		.phoneNumber("21986861613")
		.neighborhood("Tijuca")
		.address("Rua Silva Perez 39/21")
		.reference("Amigo do Jaca")
		.build();

	public static final Client JOAO = Client.builder()
		.name("João")
		.phoneNumber("21986831413")
		.neighborhood("Méier")
		.address("Rua Adolfo 2334")
		.reference("Irmão do Jorel")
		.build();

	public static final Client RONALDO = Client.builder()
		.name("Ronaldo")
		.phoneNumber("21982831413")
		.neighborhood("Penha")
		.address("Rua Silva Cruz 33/200")
		.reference("Pai do Sandro")
		.build();

	public static final List<Client> CLIENTS = new ArrayList<>() {
		{
			add(FELIPE);
			add(JOAO);
			add(RONALDO);
		}
	};

	public static final Client EMPTY_CLIENT = new Client();
	public static final Client INVALID_CLIENT = Client.builder()
		.name("")
		.phoneNumber("21986861613333")
		.neighborhood("")
		.address("")
		.reference("")
		.build();
}