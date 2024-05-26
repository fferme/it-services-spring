package com.ferme.itservices.repositories;

import com.ferme.itservices.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
	Optional<OrderItem> findByDescription(String description);
}