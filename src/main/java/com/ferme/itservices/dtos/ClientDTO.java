package com.ferme.itservices.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ClientDTO(
	UUID id,

	@NotEmpty(message = "Name is required")
	@Size(min = 4, max = 40, message = "Name must be between 4 and 40 characters")
	String name,

	@NotEmpty(message = "Phone number is required")
	@Pattern(regexp = "^\\(?(\\d{2})\\)?[- ]?(\\d{4,5})[- ]?(\\d{4})$", message = "Invalid phone number format")
	@Size(min = 8, max = 11, message = "Phone number must be between 8 and 11 characters")
	String phoneNumber,

	@Size(max = 20)
	String neighborhood,

	@Size(max = 50)
	String address,

	List<OrderDTO> ordersDTO,

	@Size(max = 70)
	String reference
) {
}