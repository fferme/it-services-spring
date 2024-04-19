package com.ferme.itservices.api.repositories;

import com.ferme.itservices.api.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}