package com.ferme.itservices.security.controllers;

import com.ferme.itservices.security.dtos.AuthenticationDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	private final AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public ResponseEntity<AuthenticationDTO> login(@RequestBody @Valid AuthenticationDTO loginData) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(loginData.username(), loginData.password());
		var auth = authenticationManager.authenticate(usernamePassword);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}