package com.ferme.itservices.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.api.models.Order;
import com.ferme.itservices.security.auditing.models.AuditInfo;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@Embeddable
public record OrderItemDTO(
	UUID id,

	@NotNull
	@Convert(converter = OrderItemTypeConverter.class)
	OrderItemType orderItemType,

	@Size(max = 240)
	String description,

	@DecimalMin(value = "0.0", message = "Price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Price must be max 9999.00")
	Double price,

	@NotNull
	Boolean showInListAll,

	@JsonIgnore
	List<Order> orders,

	@Embedded
	AuditInfo auditInfo
) { }
