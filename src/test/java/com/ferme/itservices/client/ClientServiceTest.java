package com.ferme.itservices.client;

import com.ferme.itservices.api.dtos.ClientDTO;
import com.ferme.itservices.api.dtos.mappers.ClientMapper;
import com.ferme.itservices.api.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ferme.itservices.common.ClientConstants.CLIENT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ClientService.class)
public class ClientServiceTest {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientMapper clientMapper;

    @Test
    public void createClient_WithValidData_ReturnsClient() {
        ClientDTO sut = clientService.create(clientMapper.toDTO(CLIENT));

        assertThat(sut).isEqualTo(clientMapper.toDTO(CLIENT));
    }
}
