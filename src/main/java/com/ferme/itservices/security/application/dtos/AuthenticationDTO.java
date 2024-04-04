package com.ferme.itservices.security.application.dtos;

public record AuthenticationDTO(
	String username,
	String password
) {
}