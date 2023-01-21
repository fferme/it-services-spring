package com.fermesolutions.itservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fermesolutions.itservices.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    void deleteClient();
    
}
