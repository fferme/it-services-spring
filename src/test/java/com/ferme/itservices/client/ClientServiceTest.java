package com.ferme.itservices.client;

import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.repositories.ClientRepository;
import com.ferme.itservices.api.services.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.ferme.itservices.common.ClientConstants.CLIENT;
import static com.ferme.itservices.common.ClientConstants.INVALID_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Test
    public void createClient_WithValidData_ReturnsClient() {
        when(clientRepository.save(CLIENT)).thenReturn(CLIENT);

        Client sut = clientService.create(CLIENT);

        assertThat(sut).isEqualTo(CLIENT);
    }

    @Test
    public void createClient_WithInvalidData_ThrowsException() {
        when(clientRepository.save(INVALID_CLIENT)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> clientService.create(INVALID_CLIENT)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getClient_ByExistingId_ReturnsClient() {
        UUID uuid = UUID.randomUUID();
        when(clientRepository.findById(uuid)).thenReturn(Optional.of(CLIENT));

        Optional<Client> sut = clientService.findById(uuid);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(CLIENT);
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
        when(clientRepository.findByName(CLIENT.getName())).thenReturn(Optional.of(CLIENT));

        Optional<Client> sut = clientService.findByName(CLIENT.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(CLIENT);
    }

    @Test
    public void getClient_ByUnexistingName_ReturnsClient() {
        final String name = "Unexisting name";
        when(clientRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<Client> sut = clientService.findByName(name);

        assertThat(sut).isEmpty();
    }

}