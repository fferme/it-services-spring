package com.ferme.itservices.api.entities.enums.converter;

import com.ferme.itservices.api.entities.enums.OrderItemType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderItemTypeConverter implements AttributeConverter<OrderItemType, String> {

	public static OrderItemType convertToOrderItemType(String value) {
		if (value == null) { throw new IllegalArgumentException("Value cannot be null"); }

		return switch (value) {
			case "Compra de Peça" -> OrderItemType.PART_BUYOUT;
			case "Troca de Peça" -> OrderItemType.PART_EXCHANGE;
			case "Mão de Obra" -> OrderItemType.MANPOWER;
			case "Transporte Uber" -> OrderItemType.UBER_TRANSPORTATION;
			case "Transporte Ônibus" -> OrderItemType.BUS_TRANSPORTATION;
			case "Transporte Trem" -> OrderItemType.TRAIN_TRANSPORTATION;
			case "Transporte Metrô" -> OrderItemType.SUBWAY_TRANSPORTATION;
			case "Transporte Barco" -> OrderItemType.BOAT_TRANSPORTATION;
			case "Instalação App" -> OrderItemType.SOFTWARE_INSTALLATION;
			case "Desconto" -> OrderItemType.DISCOUNT;

			default -> throw new IllegalArgumentException("Invalid order item type: " + value);
		};
	}

	@Override
	public String convertToDatabaseColumn(OrderItemType orderItemType) {
		if (orderItemType == null) { throw new IllegalArgumentException("Invalid order item type: null"); }

		return orderItemType.getValue();
	}

	@Override
	public OrderItemType convertToEntityAttribute(String value) {
		for (OrderItemType itemType : OrderItemType.values()) {
			if (itemType.getValue().equals(value)) {
				return itemType;
			}
		}

		throw new IllegalArgumentException("Invalid order item type: " + value);
	}


}