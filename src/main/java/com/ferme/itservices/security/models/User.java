package com.ferme.itservices.security.models;

import com.ferme.itservices.api.utils.models.Timestamps;
import com.ferme.itservices.security.enums.UserRole;
import com.ferme.itservices.security.enums.converters.UserRoleConverter;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
@Getter
@Entity(name = "users")
@Table(name = "users")
public class User implements UserDetails {
	@Id
	@Size(min = 3, max = 30, message = "Username must be minimum 3 characters")
	@Column(length = 30, updatable = false, unique = true, nullable = false)
	private String username;

	@NotBlank
	@Size(min = 4, message = "Password must be minimum 4 characters")
	@Column(nullable = false)
	private String password;

	@NotNull
	@Column(length = 10, nullable = false)
	@Convert(converter = UserRoleConverter.class)
	private UserRole userRole;

	@Embedded
	@Valid
	@NotNull
	private Timestamps timestamps = new Timestamps();

	@Override
	public List<SimpleGrantedAuthority> getAuthorities() {
		return ((this.userRole == UserRole.ADMIN) || (this.userRole == UserRole.OWNER))
			? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"))
			: List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}