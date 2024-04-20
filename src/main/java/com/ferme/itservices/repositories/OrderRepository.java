package com.ferme.itservices.repositories;

import com.ferme.itservices.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}