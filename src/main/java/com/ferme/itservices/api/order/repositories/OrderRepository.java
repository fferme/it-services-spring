package com.ferme.itservices.api.order.repositories;

import com.ferme.itservices.api.order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}