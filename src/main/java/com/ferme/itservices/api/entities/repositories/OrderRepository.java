package com.ferme.itservices.api.entities.repositories;

import com.ferme.itservices.api.entities.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}