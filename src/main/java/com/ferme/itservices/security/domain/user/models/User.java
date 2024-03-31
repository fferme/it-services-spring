package com.ferme.itservices.security.domain.user.models;

import com.ferme.itservices.api.utils.models.Timestamps;
import com.ferme.itservices.security.domain.user.enums.converters.UserRoleConverter;
import com.ferme.itservices.security.domain.user.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
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
	@Builder.Default
	private Timestamps timestamps = new Timestamps();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
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