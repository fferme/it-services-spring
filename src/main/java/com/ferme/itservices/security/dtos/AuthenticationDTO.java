package com.ferme.itservices.security.dtos;

public record AuthenticationDTO(
	String username,
	String password
) {
}