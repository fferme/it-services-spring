package com.ferme.itservices.repositories;

import com.ferme.itservices.models.Client;
import com.ferme.itservices.models.ids.ClientId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, ClientId> {

}