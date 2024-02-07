package com.ferme.itservices.api.services;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.dtos.mappers.ClientMapper;
import com.ferme.itservices.api.repositories.ClientRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public List<ClientDTO> listAll() {
        return clientMapper.toDTOList(clientRepository.findAll());
    }

    public ClientDTO findById(@Valid @NotNull UUID id) {
        return clientRepository.findById(id).map(clientMapper::toDTO)
                               .orElseThrow(RuntimeException::new);
    }

    public ClientDTO create(@Valid @NotNull ClientDTO clientDTO) {
        return clientMapper.toDTO(clientRepository.save(clientMapper.toEntity(clientDTO)));
    }

    public ClientDTO update(@NotNull UUID id, @Valid @NotNull ClientDTO newClientDTO) {
        return clientRepository.findById(id)
                               .map(clientFound -> {
                                   clientFound.setNeighborhood(newClientDTO.getNeighborhood());
                                   clientFound.setReference(newClientDTO.getReference());

                                   return clientMapper.toDTO(clientRepository.save(clientFound));

                               }).orElseThrow(RuntimeException::new);
    }

    public void deleteById(@NotNull UUID id) {
        clientRepository.delete(clientRepository.findById(id)
                                                .orElseThrow(RuntimeException::new));
    }

    public void deleteAll() {
        clientRepository.deleteAll();
    }
}