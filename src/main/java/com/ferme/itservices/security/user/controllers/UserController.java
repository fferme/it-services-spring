package com.ferme.itservices.security.user.controllers;

import com.ferme.itservices.api.models.OrderItem;
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

		@Operation(summary = "Recupera lista de usuários", method = "GET")
		@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso ao recuperar lista de usuários",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
				}),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
		@GetMapping
		public List<User> listAll() {
			return userService.listAll();
		}

		@Operation(summary = "Recupera usuário a partir do nome", method = "GET")
		@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso ao recuperar usuário pelo nome",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
			@ApiResponse(responseCode = "400", description = "Nome de usuário informado inválido", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado com nome informado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
		@GetMapping("/{username}")
		public Optional<User> findById(@PathVariable @NotBlank String username) {
			return userService.findById(username);
		}

		@Operation(summary = "Cria novo usuário", method = "POST")
		@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Sucesso ao criar usuário",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
			@ApiResponse(responseCode = "404", description = "Requisição não encontrada", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
		@PostMapping
		@ResponseStatus(code = HttpStatus.CREATED)
		public User create(@RequestBody @Valid @NotNull User user) {
			return userService.create(user);
		}

		@Operation(summary = "Atualiza usuário existente", method = "PUT")
		@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Sucesso ao atualizar usuário",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado com nome informado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
		@PutMapping("/{username}")
		public User update(@PathVariable @NotBlank String username, @RequestBody @Valid @NotNull User newUser) {
			return userService.update(username, newUser);
		}

		@Operation(summary = "Deleta usuário existente", method = "DELETE")
		@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado com nome informado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
		@DeleteMapping("/{username}")
		@ResponseStatus(code = HttpStatus.NO_CONTENT)
		public void deleteById(@PathVariable @NotBlank String username) {
			userService.deleteById(username);
		}

		@Operation(summary = "Deleta todos usuários existentes", method = "DELETE")
		@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuários deletados com sucesso",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor",
				content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
				})
		})
		@DeleteMapping
		@ResponseStatus(code = HttpStatus.NO_CONTENT)
		public void deleteAll() {
			userService.deleteAll();
		}
}