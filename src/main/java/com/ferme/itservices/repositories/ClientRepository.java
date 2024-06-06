package com.ferme.itservices.repositories;

import com.ferme.itservices.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
	Optional<Client> findByName(String name);
}