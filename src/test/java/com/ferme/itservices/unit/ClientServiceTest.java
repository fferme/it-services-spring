package com.ferme.itservices.unit;

import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.repositories.ClientRepository;
import com.ferme.itservices.api.services.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.ferme.itservices.common.ClientConstants.INVALID_CLIENT;
import static com.ferme.itservices.common.ClientConstants.VALID_CLIENT;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Test
    public void createClient_WithValidData_ReturnsClient() {
        when(clientRepository.save(VALID_CLIENT)).thenReturn(VALID_CLIENT);

        Client sut = clientService.create(VALID_CLIENT);

        assertThat(sut).isEqualTo(VALID_CLIENT);
    }

    @Test
    public void createClient_WithInvalidData_ThrowsException() {
        when(clientRepository.save(INVALID_CLIENT)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> clientService.create(INVALID_CLIENT)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getClient_ByExistingId_ReturnsClient() {
        UUID uuid = UUID.randomUUID();
        when(clientRepository.findById(uuid)).thenReturn(Optional.of(VALID_CLIENT));

        Optional<Client> sut = clientService.findById(uuid);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(VALID_CLIENT);
    }

    @Test
    public void getClient_ByUnexistingId_ReturnsEmpty() {
        UUID uuid = UUID.randomUUID();
        when(clientRepository.findById(uuid)).thenReturn(Optional.empty());

        Optional<Client> sut = clientService.findById(uuid);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getClient_ByExistingName_ReturnsClient() {
        when(clientRepository.findByName(VALID_CLIENT.getName())).thenReturn(Optional.of(VALID_CLIENT));

        Optional<Client> sut = clientService.findByName(VALID_CLIENT.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(VALID_CLIENT);
    }

    @Test
    public void getClient_ByUnexistingName_ReturnsClient() {
        final String name = "Unexisting name";
        when(clientRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<Client> sut = clientService.findByName(name);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listClients_ReturnsAllClients() {
        List<Client> clients = new ArrayList<>() {
            { add(VALID_CLIENT); }
        };
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> sut = clientRepository.findAll();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.getFirst()).isEqualTo(VALID_CLIENT);
    }

    @Test
    public void listClients_ReturnsNoClients() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());

        List<Client> sut = clientRepository.findAll();

        assertThat(sut).isEmpty();
    }

    @Test
    public void deleteClient_WithExistingId_doesNotThrowAnyException() {
        UUID uuid = UUID.randomUUID();
        assertThatCode(() -> clientService.deleteById(uuid)).doesNotThrowAnyException();
    }

    @Test
    public void deleteClient_WithUnexistingId_ThrowsException() {
        UUID unexistingUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        doThrow(new RuntimeException()).when(clientRepository).deleteById(unexistingUUID);

        assertThatThrownBy(() -> clientService.deleteById(unexistingUUID)).isInstanceOf(RuntimeException.class);
    }


}