package com.fermesolutions.itservices.repository;

import com.fermesolutions.itservices.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComputerRepository extends JpaRepository<Computer, Long> {
}
