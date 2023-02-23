package com.fermesolutions.itservices.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import com.fermesolutions.itservices.dto.ClientDTO;
import com.fermesolutions.itservices.dto.mapper.ClientMapper;
import com.fermesolutions.itservices.exception.RecordNotFoundException;
import com.fermesolutions.itservices.model.Client;
import com.fermesolutions.itservices.repository.ClientRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }
    
    public List<ClientDTO> listAll() {
        return clientRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
        .stream()
        .map(clientMapper::toDTO)
        .collect(Collectors.toList());
    }

    public ClientDTO findById(@PathVariable @NotNull @Positive Long clientId) {
        return clientRepository.findById(clientId)
        .map(clientMapper::toDTO)
        .orElseThrow(() -> new RecordNotFoundException(clientId));
    }

    public ClientDTO create(@Valid @NotNull ClientDTO clientDTO) {
        return clientMapper.toDTO(clientRepository.save(clientMapper.toEntity(clientDTO)));
    }

    public ClientDTO update(@Positive @NotNull Long clientId, @Valid @NotNull ClientDTO newClientDTO) {
        return clientRepository.findById(clientId)
                .map(clientDTOFound -> {
                    clientDTOFound.setName(newClientDTO.name());
                    clientDTOFound.setPhoneNumber(newClientDTO.phoneNumber());
                    clientDTOFound.setNeighbourhood(newClientDTO.neighbourhood());
                    clientDTOFound.setReference(newClientDTO.reference());

                    return clientMapper.toDTO(clientRepository.save(clientDTOFound));
                }).orElseThrow(() -> new RecordNotFoundException(clientId));
    }

    public void delete(@PathVariable @NotNull @Positive Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RecordNotFoundException(clientId));
        if (!client.getOrders().isEmpty()) {
            clientRepository.removeClientFromOrders(clientId);
        }
        
        clientRepository.delete(clientRepository.findById(clientId)
                .orElseThrow(() -> new RecordNotFoundException(clientId)));
    }
}
