package com.ferme.itservices.orderItem.unit;

import com.ferme.itservices.api.orderItem.enums.OrderItemType;
import com.ferme.itservices.api.orderItem.enums.converter.OrderItemTypeConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderItemTypeConverterTest {
	@InjectMocks
	private OrderItemTypeConverter converter;

	@Test
	public void convertOrderItemTypeValue_WithValidValue_ReturnsToOrderItemType() {
		assertEquals(OrderItemType.PART_BUYOUT, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.PART_BUYOUT.getValue()));
		assertEquals(OrderItemType.PART_EXCHANGE, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.PART_EXCHANGE.getValue()));
		assertEquals(OrderItemType.MANPOWER, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.MANPOWER.getValue()));
		assertEquals(OrderItemType.BOAT_TRANSPORTATION, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.BOAT_TRANSPORTATION.getValue()));
		assertEquals(OrderItemType.BUS_TRANSPORTATION, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.BUS_TRANSPORTATION.getValue()));
		assertEquals(OrderItemType.DISCOUNT, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.DISCOUNT.getValue()));
		assertEquals(OrderItemType.SOFTWARE_INSTALLATION, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.SOFTWARE_INSTALLATION.getValue()));
		assertEquals(OrderItemType.SUBWAY_TRANSPORTATION, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.SUBWAY_TRANSPORTATION.getValue()));
		assertEquals(OrderItemType.TRAIN_TRANSPORTATION, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.TRAIN_TRANSPORTATION.getValue()));
		assertEquals(OrderItemType.UBER_TRANSPORTATION, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.UBER_TRANSPORTATION.getValue()));
	}

	@Test
	public void convertToOrderItemTypeValue_WithInvalid_ReturnsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> OrderItemTypeConverter.convertToOrderItemType(null));
		assertThrows(IllegalArgumentException.class, () -> OrderItemTypeConverter.convertToOrderItemType("Invalid"));
	}

	@Test
	public void convertToDatabaseColumn_WithValidValue_ReturnsOrderItemTypeValue() {
		assertEquals(OrderItemType.PART_BUYOUT.getValue(), converter.convertToDatabaseColumn(OrderItemType.PART_BUYOUT));
		assertEquals(OrderItemType.PART_EXCHANGE.getValue(), converter.convertToDatabaseColumn(OrderItemType.PART_EXCHANGE));
		assertEquals(OrderItemType.MANPOWER.getValue(), converter.convertToDatabaseColumn(OrderItemType.MANPOWER));
		assertEquals(OrderItemType.BOAT_TRANSPORTATION.getValue(), converter.convertToDatabaseColumn(OrderItemType.BOAT_TRANSPORTATION));
		assertEquals(OrderItemType.BUS_TRANSPORTATION.getValue(), converter.convertToDatabaseColumn(OrderItemType.BUS_TRANSPORTATION));
		assertEquals(OrderItemType.DISCOUNT.getValue(), converter.convertToDatabaseColumn(OrderItemType.DISCOUNT));
		assertEquals(OrderItemType.SOFTWARE_INSTALLATION.getValue(), converter.convertToDatabaseColumn(OrderItemType.SOFTWARE_INSTALLATION));
		assertEquals(OrderItemType.SUBWAY_TRANSPORTATION.getValue(), converter.convertToDatabaseColumn(OrderItemType.SUBWAY_TRANSPORTATION));
		assertEquals(OrderItemType.TRAIN_TRANSPORTATION.getValue(), converter.convertToDatabaseColumn(OrderItemType.TRAIN_TRANSPORTATION));
		assertEquals(OrderItemType.UBER_TRANSPORTATION.getValue(), converter.convertToDatabaseColumn(OrderItemType.UBER_TRANSPORTATION));
	}

	@Test
	public void convertToDatabaseColumn_WithInvalidValue_ReturnsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> converter.convertToDatabaseColumn(null));
	}

	@Test
	public void convertToEntityAttribute_WithValidData_ReturnsOrderItemType() {
		assertEquals(OrderItemType.PART_BUYOUT, converter.convertToEntityAttribute(OrderItemType.PART_BUYOUT.getValue()));
		assertEquals(OrderItemType.PART_EXCHANGE, converter.convertToEntityAttribute(OrderItemType.PART_EXCHANGE.getValue()));
		assertEquals(OrderItemType.MANPOWER, converter.convertToEntityAttribute(OrderItemType.MANPOWER.getValue()));
		assertEquals(OrderItemType.BOAT_TRANSPORTATION, converter.convertToEntityAttribute(OrderItemType.BOAT_TRANSPORTATION.getValue()));
		assertEquals(OrderItemType.BUS_TRANSPORTATION, converter.convertToEntityAttribute(OrderItemType.BUS_TRANSPORTATION.getValue()));
		assertEquals(OrderItemType.DISCOUNT, converter.convertToEntityAttribute(OrderItemType.DISCOUNT.getValue()));
		assertEquals(OrderItemType.SOFTWARE_INSTALLATION, converter.convertToEntityAttribute(OrderItemType.SOFTWARE_INSTALLATION.getValue()));
		assertEquals(OrderItemType.SUBWAY_TRANSPORTATION, converter.convertToEntityAttribute(OrderItemType.SUBWAY_TRANSPORTATION.getValue()));
		assertEquals(OrderItemType.TRAIN_TRANSPORTATION, converter.convertToEntityAttribute(OrderItemType.TRAIN_TRANSPORTATION.getValue()));
		assertEquals(OrderItemType.UBER_TRANSPORTATION, converter.convertToEntityAttribute(OrderItemType.UBER_TRANSPORTATION.getValue()));
	}

	@Test
	public void convertToEntityAttribute_WithInvalidValue_ReturnsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("Invalid"));
		assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute(null));
	}

}