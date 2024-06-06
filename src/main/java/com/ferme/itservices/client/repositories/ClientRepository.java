package com.ferme.itservices.client.repositories;

import com.ferme.itservices.client.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
	Optional<Client> findByName(String name);
}