package com.ferme.itservices.client;

import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.models.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTO;
import static com.ferme.itservices.dtos.mappers.ClientMapper.toClientDTOList;

public class ClientConstants {

	public static final UUID CLIENT_A_UUID = UUID.fromString("d6a0eb46-d011-4fb1-95ab-44f6d415d9bf");
	public static final Client CLIENT_A = Client.builder()
		.name("Felipe")
		.phoneNumber("21986861613")
		.neighborhood("Tijuca")
		.address("Rua Silva Perez 39/21")
		.reference("Amigo do Jaca")
		.build();
	public static final ClientDTO CLIENT_A_DTO = toClientDTO(CLIENT_A);

	public static final Client CLIENT_B = Client.builder()
		.name("João")
		.phoneNumber("21986831413")
		.neighborhood("Méier")
		.address("Rua Adolfo 2334")
		.reference("Irmão do Jorel")
		.build();
	public static final ClientDTO CLIENT_B_DTO = toClientDTO(CLIENT_B);

	public static final Client CLIENT_C = Client.builder()
		.name("Ronaldo")
		.phoneNumber("21982831413")
		.neighborhood("Penha")
		.address("Rua Silva Cruz 33/200")
		.reference("Pai do Sandro")
		.build();
	public static final ClientDTO CLIENT_C_DTO = toClientDTO(CLIENT_C);

	public static final Client NEW_CLIENT = Client.builder()
		.name("New Name")
		.phoneNumber("21989653626")
		.neighborhood("New Neighborhood")
		.address("New Address")
		.reference("New Reference")
		.build();
	public static final ClientDTO NEW_CLIENT_DTO = toClientDTO(NEW_CLIENT);

	public static final Client CLIENT_WITH_ID = Client.builder()
		.id(UUID.fromString("f589bb9a-3c55-459d-8781-cbf8020a6ec6"))
		.name("João UUID")
		.phoneNumber("21984453626")
		.neighborhood("Saara")
		.address("Rua Silva Mourão")
		.reference("Irmão do pai")
		.build();

	public static final Client EMPTY_CLIENT = new Client();
	public static final ClientDTO EMPTY_CLIENT_DTO = toClientDTO(EMPTY_CLIENT);

	public static final Client INVALID_CLIENT = Client.builder()
		.name("2")
		.phoneNumber("2198686161saewq3333")
		.neighborhood("")
		.address("")
		.reference("")
		.build();
	public static final ClientDTO INVALID_CLIENT_DTO = toClientDTO(INVALID_CLIENT);

	public static final List<Client> CLIENTS = new ArrayList<>() {
		{
			add(CLIENT_A);
			add(CLIENT_B);
			add(CLIENT_C);
		}
	};
	public static final List<ClientDTO> CLIENTS_DTO = toClientDTOList(CLIENTS);
}