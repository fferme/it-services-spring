package com.ferme.itservices.api.enums.converter;

import com.ferme.itservices.api.enums.OrderItemType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderItemTypeConverter implements AttributeConverter<OrderItemType, String> {

    public static OrderItemType convertOrderItemTypeValue(String value) {
        return (value == null)
            ? null
            : switch (value) {
            case "Compra de Peça" -> OrderItemType.PART_BUYOUT;
            case "Troca de Peça" -> OrderItemType.PART_EXCHANGE;
            case "Mão de Obra" -> OrderItemType.MANPOWER;
            case "Transporte" -> OrderItemType.CARRIAGE;
            default -> throw new IllegalArgumentException("Invalid order item type: " + value);
        };
    }

    @Override
    public String convertToDatabaseColumn(OrderItemType orderItemType) {
        return (orderItemType == null)
            ? null
            : orderItemType.getValue();
    }

    @Override
    public OrderItemType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        for (OrderItemType itemType : OrderItemType.values()) {
            if (itemType.getValue().equals(value)) {
                return itemType;
            }
        }

        throw new IllegalArgumentException("Invalid order item type: " + value);
    }

}