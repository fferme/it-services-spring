package com.ferme.itservices.client.dtos.mappers;

import com.ferme.itservices.client.dtos.ClientDTO;
import com.ferme.itservices.client.models.Client;
import com.ferme.itservices.order.dtos.OrderDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientMapper {
	public static Client toClient(ClientDTO clientDTO) {
		if (clientDTO == null) { return null; }

		Client client = new Client();
		client.setName(clientDTO.name());
		client.setPhoneNumber(clientDTO.phoneNumber());
		client.setNeighborhood(clientDTO.neighborhood());
		client.setAddress(clientDTO.address());
		client.setReference(clientDTO.reference());

		return client;
	}

	public static ClientDTO toClientDTO(Client client) {
		if (client == null) { return null; }

		List<OrderDTO> ordersDTO = new ArrayList<>();
		if (client.getOrders() != null) {
			ordersDTO = client.getOrders()
				.stream()
				.map(order -> new OrderDTO(
					order.getId(),
					order.getHeader(),
					order.getDeviceName(),
					order.getDeviceSN(),
					order.getIssues(),
					null,
					null,
					order.getTotalPrice()
				))
				.toList();
		}

		return new ClientDTO(
			client.getId(),
			client.getName(),
			client.getPhoneNumber(),
			client.getNeighborhood(),
			client.getAddress(),
			ordersDTO,
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