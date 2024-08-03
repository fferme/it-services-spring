package com.ferme.itservices.security.application.controllers;

import com.ferme.itservices.api.entities.models.Client;
import com.ferme.itservices.security.application.dtos.AuthenticationDTO;
import com.ferme.itservices.security.application.dtos.LoginResponseDTO;
import com.ferme.itservices.security.application.services.TokenService;
import com.ferme.itservices.security.user.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/auth", produces = {"application/json"})
@Tag(name = "Authentication Controller")
public class AuthenticationController {
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	@Operation(summary = "Logs in the user and returns a token", method = "POST")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201", description = "Successfully logged in with the provided user",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))}),
			@ApiResponse(responseCode = "404", description = "Request not found", content = @Content()),
			@ApiResponse(
				responseCode = "500", description = "Internal server error",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
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
