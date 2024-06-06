package com.ferme.itservices.api.repositories;

import com.ferme.itservices.api.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}