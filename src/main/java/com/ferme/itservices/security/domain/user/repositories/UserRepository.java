package com.ferme.itservices.security.domain.user.repositories;

import com.ferme.itservices.security.domain.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
	UserDetails findByUsername(String username);
}