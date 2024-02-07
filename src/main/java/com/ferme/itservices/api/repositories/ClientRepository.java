package com.ferme.itservices.api.repositories;

import com.ferme.itservices.api.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

}