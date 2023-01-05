package com.fermesolutions.itservices.repository;

import com.fermesolutions.itservices.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
