package com.ferme.itservices.security.domain.user.services;

import com.ferme.itservices.api.exceptions.RecordNotFoundException;
import com.ferme.itservices.security.domain.user.models.User;
import com.ferme.itservices.security.domain.user.repositories.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Valid
@Service
@AllArgsConstructor
public class UserService {
	private UserRepository userRepository;

	public List<User> listAll() {
		List<User> users = userRepository.findAll();

		return users.stream()
			.sorted(Comparator.comparing(User::getUsername))
			.collect(Collectors.toList());
	}

	public Optional<User> findById(@NotBlank String username) {
		return userRepository.findById(username);
	}

	public User create(@Valid @NotNull User user) {
		return userRepository.save(user);
	}

	public User update(@NotBlank String username, @Valid @NotNull User newUser) {
		return userRepository.findById(username)
			.map(userFound -> {
				userFound.setPassword(newUser.getPassword());

				return userRepository.save(userFound);

			}).orElseThrow(() -> new RecordNotFoundException(User.class, username));
	}

	public void deleteById(@NotBlank String username) {
		userRepository.deleteById(username);
	}

	public void deleteAll() {
		userRepository.deleteAll();
	}
}