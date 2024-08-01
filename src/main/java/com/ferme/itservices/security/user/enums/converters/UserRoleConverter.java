package com.ferme.itservices.security.user.enums.converters;

import com.ferme.itservices.security.user.enums.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

	@Override
	public String convertToDatabaseColumn(UserRole userRole) {
		return (userRole == null) ? null
			: userRole.getValue();
	}

	@Override
	public UserRole convertToEntityAttribute(String value) {
		return (value == null) ? null
			: Stream.of(UserRole.values())
			.filter(ur -> ur.getValue().equals(value))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	public static UserRole convertToUserRoleValue(String value) {
		return (value == null)
			? null
			: switch (value) {
			case "Owner" -> UserRole.OWNER;
			case "Admin" -> UserRole.ADMIN;
			case "Guest" -> UserRole.GUEST;
			default -> throw new IllegalArgumentException("Tipo inválido de permissão do usuário: " + value);
		};
	}
}