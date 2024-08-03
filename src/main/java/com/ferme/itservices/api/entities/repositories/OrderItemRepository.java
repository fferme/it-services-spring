package com.ferme.itservices.api.entities.repositories;

import com.ferme.itservices.api.entities.enums.OrderItemType;
import com.ferme.itservices.api.entities.models.OrderItem;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
	@Nonnull
	@Query("SELECT OI FROM orderItems OI WHERE OI.showInListAll = true")
	List<OrderItem> findAll();

	Optional<OrderItem> findByDescription(String description);

	Optional<OrderItem> findByOrderItemTypeAndDescriptionAndPrice(OrderItemType orderItemType, String description, Double price);
}