package com.fermesolutions.itservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fermesolutions.itservices.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    
}
