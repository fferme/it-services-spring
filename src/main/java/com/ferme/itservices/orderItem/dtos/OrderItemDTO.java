package com.ferme.itservices.orderItem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ferme.itservices.order.models.Order;
import com.ferme.itservices.orderItem.enums.OrderItemType;
import com.ferme.itservices.orderItem.enums.converter.OrderItemTypeConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderItemDTO(
	UUID id,

	@NotNull
	@Convert(converter = OrderItemTypeConverter.class)
	OrderItemType orderItemType,

	@Size(max = 70)
	String description,

	@DecimalMin(value = "0.0", message = "Price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Price must be max 9999.00")
	Double price,

	@JsonIgnore
	List<Order> orders
) { }
