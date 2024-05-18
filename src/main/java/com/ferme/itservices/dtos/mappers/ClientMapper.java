package com.ferme.itservices.dtos.mappers;

import com.ferme.itservices.dtos.ClientDTO;
import com.ferme.itservices.models.Client;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientMapper {
	public static Client toClient(ClientDTO clientDTO) {
		return new Client(
			clientDTO.id(),
			clientDTO.name(),
			clientDTO.phoneNumber(),
			clientDTO.neighborhood(),
			clientDTO.address(),
			clientDTO.orders(),
			clientDTO.reference()
		);
	}

	public static ClientDTO toClientDTO(Client client) {
		return new ClientDTO(
			client.getId(),
			client.getName(),
			client.getPhoneNumber(),
			client.getNeighborhood(),
			client.getAddress(),
			client.getOrders(),
			client.getReference()
		);
	}

	public static List<ClientDTO> toClientDTOList(List<Client> clients) {
		List<ClientDTO> dtos = new ArrayList<>();
		for (Client client : clients) {
			dtos.add(toClientDTO(client));
		}

		return dtos;
	}
}