package com.ferme.itservices.api.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum OrderItemType {
	@JsonProperty("Compra de Peça")
	PART_BUYOUT("Compra de Peça"),

	@JsonProperty("Troca de Peça")
	PART_EXCHANGE("Troca de Peça"),

	@JsonProperty("Mão de Obra")
	MANPOWER("Mão de Obra"),

	@JsonProperty("Transporte Ida")
	CARRIAGE_GOING("Transporte Ida"),

	@JsonProperty("Transporte Volta")
	CARRIAGE_BACK("Transporte Volta");

	private final String value;
}