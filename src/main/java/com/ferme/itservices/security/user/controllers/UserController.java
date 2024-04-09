package com.ferme.itservices.security.user.controllers;

import com.ferme.itservices.security.user.models.User;
import com.ferme.itservices.security.user.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@RestController
@Transactional
@CrossOrigin(origins = "*")
@RequestMapping(value = "/auth/users", produces = {"application/json"})
public class UserController {
		private UserService userService;

		@GetMapping
		public List<User> listAll() {
			return userService.listAll();
		}

		@GetMapping("/{username}")
		public Optional<User> findById(@PathVariable @NotBlank String username) {
			return userService.findById(username);
		}

		@PostMapping
		@ResponseStatus(code = HttpStatus.CREATED)
		public User create(@RequestBody @Valid @NotNull User user) {
			return userService.create(user);
		}

		@PutMapping("/{username}")
		public User update(@PathVariable @NotBlank String username, @RequestBody @Valid @NotNull User newUser) {
			return userService.update(username, newUser);
		}

		@DeleteMapping("/{username}")
		@ResponseStatus(code = HttpStatus.NO_CONTENT)
		public void deleteById(@PathVariable @NotBlank String username) {
			userService.deleteById(username);
		}

		@DeleteMapping
		@ResponseStatus(code = HttpStatus.NO_CONTENT)
		public void deleteAll() {
			userService.deleteAll();
		}
}