package com.ferme.itservices.client;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.dtos.mappers.ClientMapper;
import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.repositories.ClientRepository;
import com.ferme.itservices.api.services.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ferme.itservices.common.ClientConstants.CLIENT;
import static org.assertj.core.api.Assertions.assertThat;
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
}