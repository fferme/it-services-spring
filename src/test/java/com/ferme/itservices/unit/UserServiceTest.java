package com.ferme.itservices.unit;

import com.ferme.itservices.security.models.User;
import com.ferme.itservices.security.repositories.UserRepository;
import com.ferme.itservices.security.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ferme.itservices.common.UserConstants.INVALID_USER;
import static com.ferme.itservices.common.UserConstants.VALID_USER;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Test
	public void createUser_WithValidData_ReturnsUser() {
		when(userRepository.save(VALID_USER)).thenReturn(VALID_USER);

		User sut = userService.create(VALID_USER);

		assertThat(sut).isEqualTo(VALID_USER);
	}

	@Test
	public void createUser_WithInvalidData_ThrowsException() {
		assertThatThrownBy(() -> userService.create(INVALID_USER)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getUser_ByExistingId_ReturnsUser() {
		String username = "Teste";
		when(userRepository.findById(username)).thenReturn(Optional.of(VALID_USER));

		Optional<User> sut = userService.findById(username);

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(VALID_USER);
	}

	@Test
	public void getUser_ByUnexistingId_ReturnsEmpty() {
		String username = "Teste";
		when(userRepository.findById(username)).thenReturn(Optional.empty());

		Optional<User> sut = userService.findById(username);

		assertThat(sut).isEmpty();
	}

	@Test
	public void getUser_ByExistingName_ReturnsUser() {
		when(userRepository.findById(VALID_USER.getUsername())).thenReturn(Optional.of(VALID_USER));

		Optional<User> sut = userService.findById(VALID_USER.getUsername());

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(VALID_USER);
	}

	@Test
	public void getUser_ByUnexistingName_ReturnsEmpty() {
		final String username = "Unexisting username";
		when(userRepository.findById(username)).thenReturn(Optional.empty());

		Optional<User> sut = userService.findById(username);

		assertThat(sut).isEmpty();
	}

	@Test
	public void listUsers_ReturnsAllUsers() {
		List<User> users = new ArrayList<>() {
			{ add(VALID_USER); }
		};
		when(userRepository.findAll()).thenReturn(users);

		List<User> sut = userRepository.findAll();

		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSize(1);
		assertThat(sut.getFirst()).isEqualTo(VALID_USER);
	}

	@Test
	public void listUsers_ReturnsNoUsers() {
		when(userRepository.findAll()).thenReturn(Collections.emptyList());

		List<User> sut = userRepository.findAll();

		assertThat(sut).isEmpty();
	}

	@Test
	public void deleteUser_WithExistingId_doesNotThrowAnyException() {
		String username = "Teste";
		assertThatCode(() -> userService.deleteById(username)).doesNotThrowAnyException();
	}

	@Test
	public void deleteUser_WithUnexistingId_ThrowsException() {
		String username = "Teste";
		doThrow(new RuntimeException()).when(userRepository).deleteById(username);

		assertThatThrownBy(() -> userService.deleteById(username)).isInstanceOf(RuntimeException.class);
	}
}