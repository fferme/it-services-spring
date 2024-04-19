package com.ferme.itservices.api.repositories;

import com.ferme.itservices.api.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByName(String name);
}