package com.ferme.itservices.api.enums.converter;

import com.ferme.itservices.api.enums.OrderItemType;
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
			case "Transporte Ida" -> OrderItemType.CARRIAGE_GOING;
			case "Transporte Volta" -> OrderItemType.CARRIAGE_BACK;
			case "Desconto Volta" -> OrderItemType.DISCOUNT;

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