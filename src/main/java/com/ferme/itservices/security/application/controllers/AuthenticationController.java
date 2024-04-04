package com.ferme.itservices.security.application.controllers;

import com.ferme.itservices.jwt_auth.user.models.User;
import com.ferme.itservices.security.application.dtos.AuthenticationDTO;
import com.ferme.itservices.security.application.dtos.LoginResponseDTO;
import com.ferme.itservices.security.application.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
		try {
			UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
			Authentication auth = authenticationManager.authenticate(usernamePassword);
			User user = (User) auth.getPrincipal();
			String token = tokenService.generateToken(user);

			return new ResponseEntity<>(new LoginResponseDTO(user.getUsername(), token), HttpStatus.OK);
		} catch (AuthenticationException e) {
			throw new RuntimeException("Error during token authorization process", e);
		}
	}
}