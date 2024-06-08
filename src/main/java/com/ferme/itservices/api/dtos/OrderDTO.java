package com.ferme.itservices.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderDTO(
	UUID id,

	@Size(max = 95)
	String header,

	@NotBlank
	@Size(min = 4, max = 35)
	String deviceName,

	@NotBlank
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

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-3")
	LocalDateTime createdAt
) {
}
