package com.ferme.itservices.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferme.itservices.security.auditing.models.AuditInfo;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@Embeddable
public record OrderDTO(
	UUID id,

	@Size(max = 90)
	String header,

	@Size(max = 90)
	String deviceName,

	@Size(max = 35)
	String deviceSN,

	@Size(max = 250)
	String issues,

	@JsonProperty("client")
	ClientDTO clientDTO,

	@JsonProperty("orderItems")
	List<OrderItemDTO> orderItemsDTO,

	@DecimalMin(value = "0.0")
	@DecimalMax(value = "9999.00")
	Double totalPrice,

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT-3")
	LocalDate emitedAt,

	@Embedded
	AuditInfo auditInfo
) implements Serializable {
}
