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

	@JsonProperty("Transporte Uber")
	UBER_TRANSPORTATION("Transporte Uber"),

	@JsonProperty("Transporte Ônibus")
	BUS_TRANSPORTATION("Transporte Ônibus"),

	@JsonProperty("Transporte Trem")
	TRAIN_TRANSPORTATION("Transporte Trem"),

	@JsonProperty("Transporte Metrô")
	SUBWAY_TRANSPORTATION("Transporte Metrô"),

	@JsonProperty("Transporte Barco")
	BOAT_TRANSPORTATION("Transporte Barco"),

	@JsonProperty("Instalação App")
	SOFTWARE_INSTALLATION("Instalação App"),

	@JsonProperty("Desconto")
	DISCOUNT("Desconto");

	private final String value;
}