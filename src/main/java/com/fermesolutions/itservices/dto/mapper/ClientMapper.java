package com.fermesolutions.itservices.dto.mapper;

import org.springframework.stereotype.Component;

import com.fermesolutions.itservices.dto.ClientDTO;
import com.fermesolutions.itservices.model.Client;

@Component
public class ClientMapper {
    public ClientDTO toDTO(Client client) {
        if (client == null) {
            return null;
        }
        return new ClientDTO(
            client.getId(), 
            client.getName(), 
            client.getGender(), 
            client.getPhoneNumber(), 
            client.getNeighbourhood(), 
            client.getReference(), 
            client.getOrders());
    }

    public Client toEntity(ClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }

        Client client = new Client();
        if (clientDTO.id() != null) {
            client.setId(clientDTO.id());
        }
        client.setName(clientDTO.name());
        client.setGender(clientDTO.gender());
        client.setPhoneNumber(clientDTO.phoneNumber());
        client.setNeighbourhood(clientDTO.neighbourhood());
        client.setReference(clientDTO.reference());
        client.setOrders(clientDTO.orders());

        return client;
    }
}
