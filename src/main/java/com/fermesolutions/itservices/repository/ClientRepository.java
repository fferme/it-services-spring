package com.fermesolutions.itservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fermesolutions.itservices.model.Client;

import jakarta.transaction.Transactional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.client = null WHERE o.client.id = :clientId")
    void removeClientFromOrders(@Param("clientId") Long clientId);
}
