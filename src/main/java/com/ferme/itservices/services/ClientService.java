package com.ferme.itservices.services;

import com.ferme.itservices.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.logging.Logger;

@Validated
@Service
@AllArgsConstructor
public class ClientService {
    private static final Logger logger = Logger.getLogger(ClientService.class.getName());
    private final ClientRepository clientRepository;
}