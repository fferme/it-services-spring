package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.models.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public Client toEntity(ClientDTO clientDTO) {
        return (clientDTO == null)
            ? null
            : Client.builder()
                    .id(clientDTO.getId())
                    .name(clientDTO.getName())
                    .phoneNumber(clientDTO.getPhoneNumber())
                    .address(clientDTO.getAddress())
                    .neighborhood(clientDTO.getNeighborhood())
                    .reference(clientDTO.getReference())
                    .createdAt(clientDTO.getCreatedAt())
                    .updatedAt(clientDTO.getUpdatedAt())
                    .build();
    }

    public ClientDTO toDTO(Client client) {
        return (client == null)
            ? null
            : ClientDTO.builder()
                       .id(client.getId())
                       .name(client.getName())
                       .phoneNumber(client.getPhoneNumber())
                       .address(client.getAddress())
                       .neighborhood(client.getNeighborhood())
                       .reference(client.getReference())
                       .createdAt(client.getCreatedAt())
                       .updatedAt(client.getUpdatedAt())
                       .build();
    }
}