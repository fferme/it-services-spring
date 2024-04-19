package com.ferme.itservices.integration.repository;

import com.ferme.itservices.security.models.User;
import com.ferme.itservices.security.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.ferme.itservices.common.UserConstants.INVALID_USER;
import static com.ferme.itservices.common.UserConstants.VALID_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void createUser_WithValidData_ReturnsUser() {
		User user = userRepository.save(VALID_USER);

		User sut = testEntityManager.find(User.class, user.getUsername());

		assertThat(sut).isNotNull();
		assertThat(sut.getUsername()).isEqualTo(VALID_USER.getUsername());
		assertThat(sut.getPassword()).isEqualTo(VALID_USER.getPassword());
		assertThat(sut.getUserRole()).isEqualTo(VALID_USER.getUserRole());
	}

	@Test
	public void createUser_WithInvalidData_ThrowsException() {
		User user = new User();
		assertThatThrownBy(() -> userRepository.save(user)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> userRepository.save(INVALID_USER)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void createUser_WithExistingUsername_ThrowsException() {
		User user = testEntityManager.persistFlushFind(VALID_USER);
		testEntityManager.detach(user);
		user.setUsername(null);

		assertThatThrownBy(() -> userRepository.save(user)).isInstanceOf(RuntimeException.class);
	}
}