package com.ferme.itservices.api.repositories;

import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.models.OrderItem;
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