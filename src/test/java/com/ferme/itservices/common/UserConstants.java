package com.ferme.itservices.common;

import com.ferme.itservices.security.enums.UserRole;
import com.ferme.itservices.security.models.User;

public class UserConstants {
	public static final User VALID_USER = User.builder()
		.username("jpaulo")
		.password("Teste123")
		.userRole(UserRole.GUEST)
		.build();

	public static final User INVALID_USER = User.builder()
		.username("")
		.password("")
		.userRole(null)
		.build();
}