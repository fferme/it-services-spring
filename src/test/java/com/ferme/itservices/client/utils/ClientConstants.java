package com.ferme.itservices.client.utils;

import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.models.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTO;
import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTOList;

public class ClientConstants {
	public static ClientConstants instance;

	private ClientConstants() { }

	public static ClientConstants getInstance() {
		return (instance == null)
			? instance = new ClientConstants()
			: instance;
	}

	public final static UUID CLIENT_A_UUID = UUID.fromString("d6a0eb46-d011-4fb1-95ab-44f6d415d9bf");
	private final UUID CLIENT_B_UUID = UUID.fromString("88e3c5f7-d0c1-4829-b5e0-61541f9aa2dc");
	private final UUID CLIENT_C_UUID = UUID.fromString("2e8d5d11-61d7-4976-b5c9-54adfea13e5b");

	public final Client CLIENT = new Client(
		CLIENT_A_UUID,
		"Felipe",
		"21986861613",
		"Tijuca",
		"Rua Silva Perez 39/21",
		null,
		"Amigo do Jaca"
	);
	public ClientDTO CLIENT_DTO = toClientDTO(CLIENT);

	public final Client NEW_CLIENT = Client.builder()
		.name("New Name")
		.phoneNumber("21989653626")
		.neighborhood("New Neighborhood")
		.address("New Address")
		.reference("New Reference")
		.build();
	public final ClientDTO NEW_CLIENT_DTO = toClientDTO(NEW_CLIENT);

	public final Client EMPTY_CLIENT = new Client();
	public final ClientDTO EMPTY_CLIENT_DTO = toClientDTO(EMPTY_CLIENT);

	public final Client INVALID_CLIENT = Client.builder()
		.name("2")
		.phoneNumber("2198686161saewq3333")
		.neighborhood("")
		.address("")
		.reference("")
		.build();
	public final ClientDTO INVALID_CLIENT_DTO = toClientDTO(INVALID_CLIENT);

	public final List<Client> CLIENTS = new ArrayList<>() {
		{
			add(new Client(
				CLIENT_A_UUID,
				"Felipe",
				"21986861613",
				"Tijuca",
				"Rua Silva Perez 39/21",
				null,
				"Amigo do Jaca"
			));
			add(new Client(
				CLIENT_B_UUID,
				"João",
				"21986831413",
				"Méier",
				"Rua Adolfo 2334",
				null,
				"Irmão do Jorel"
			));
			add(new Client(
				CLIENT_C_UUID,
				"Ronaldo",
				"21982831413",
				"Penha",
				"Rua Silva Cruz 33/200",
				null,
				"Pai do Sandro"
			));
		}
	};
	public final List<ClientDTO> CLIENTS_DTO = toClientDTOList(CLIENTS);
}