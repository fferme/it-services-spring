package com.ferme.itservices.api.dtos.mappers;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.models.Client;

import java.util.List;

public interface ClientMapper {
    Client toEntity(ClientDTO clientDTO);
    ClientDTO toDTO(Client client);
    List<ClientDTO> toDTOList(List<Client> clientList);
}