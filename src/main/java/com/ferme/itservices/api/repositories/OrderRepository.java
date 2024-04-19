package com.ferme.itservices.api.repositories;

import com.ferme.itservices.api.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}