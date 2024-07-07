package com.ferme.itservices.api.client.repositories;

import com.ferme.itservices.api.client.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
	Optional<Client> findByName(String name);

	Optional<Client> findByNameAndPhoneNumber(String name, String phoneNumber);
}