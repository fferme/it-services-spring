package com.ferme.itservices.dtos;

import com.ferme.itservices.models.Order;

import java.util.List;
import java.util.UUID;

public record ClientDTO(
	UUID id,
	String name,
	String phoneNumber,
	String neighborhood,
	String address,
	List<Order> orders,
	String reference
) {
}