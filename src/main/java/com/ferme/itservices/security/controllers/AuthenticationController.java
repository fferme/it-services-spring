package com.ferme.itservices.security.controllers;

import com.ferme.itservices.security.dtos.AuthenticationDTO;
import com.ferme.itservices.security.dtos.LoginResponseDTO;
import com.ferme.itservices.security.models.User;
import com.ferme.itservices.security.services.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO loginData) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(loginData.username(), loginData.password());
		var auth = authenticationManager.authenticate(usernamePassword);
		User user = (User) auth.getPrincipal();
		String token = tokenService.generateToken(user);

		return new ResponseEntity<>(new LoginResponseDTO(user.getUsername(), token), HttpStatus.OK);
	}
}