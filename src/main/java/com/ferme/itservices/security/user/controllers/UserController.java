package com.ferme.itservices.security.user.controllers;

import com.ferme.itservices.security.user.models.User;
import com.ferme.itservices.security.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@RestController
@Transactional
@CrossOrigin(origins = "*")
@RequestMapping(value = "/auth/users", produces = {"application/json"})
@Tag(name = "User Controller")
public class UserController {
	private UserService userService;

	@Operation(summary = "Retrieve list of users", method = "GET")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
			}),
		@ApiResponse(responseCode = "500", description = "Internal server error",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
			})
	})
	@GetMapping
	public List<User> listAll() {
		return userService.listAll();
	}

	@Operation(summary = "Retrieve user by username", method = "GET")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved user by username",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
		@ApiResponse(responseCode = "400", description = "Invalid username provided", content = @Content()),
		@ApiResponse(responseCode = "404", description = "User not found with the provided username", content = @Content()),
		@ApiResponse(responseCode = "500", description = "Internal server error",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
			})
	})
	@GetMapping("/{username}")
	public Optional<User> findById(@PathVariable @NotBlank String username) {
		return userService.findById(username);
	}

	@Operation(summary = "Create new user", method = "POST")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Successfully created user",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
		@ApiResponse(responseCode = "404", description = "Request not found", content = @Content()),
		@ApiResponse(responseCode = "500", description = "Internal server error",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
			})
	})
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public User create(@RequestBody @Valid @NotNull User user) {
		return userService.create(user);
	}

	@Operation(summary = "Update existing user", method = "PUT")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Successfully updated user",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
		@ApiResponse(responseCode = "404", description = "User not found with the provided username", content = @Content()),
		@ApiResponse(responseCode = "500", description = "Internal server error",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
			})
	})
	@PutMapping("/{username}")
	public User update(@PathVariable @NotBlank String username, @RequestBody @Valid @NotNull User newUser) {
		return userService.update(username, newUser);
	}

	@Operation(summary = "Delete existing user", method = "DELETE")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Successfully deleted user",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
		@ApiResponse(responseCode = "404", description = "User not found with the provided username", content = @Content()),
		@ApiResponse(responseCode = "500", description = "Internal server error",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
			})
	})
	@DeleteMapping("/{username}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable @NotBlank String username) {
		userService.deleteById(username);
	}

	@Operation(summary = "Delete all existing users", method = "DELETE")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Successfully deleted all users",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
		@ApiResponse(responseCode = "500", description = "Internal server error",
			content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
			})
	})
	@DeleteMapping
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteAll() {
		userService.deleteAll();
	}
}
