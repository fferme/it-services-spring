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
		assertEquals(OrderItemType.CARRIAGE_GOING, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.CARRIAGE_GOING.getValue()));
		assertEquals(OrderItemType.CARRIAGE_BACK, OrderItemTypeConverter.convertToOrderItemType(OrderItemType.CARRIAGE_BACK.getValue()));
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
		assertEquals(OrderItemType.CARRIAGE_GOING.getValue(), converter.convertToDatabaseColumn(OrderItemType.CARRIAGE_GOING));
		assertEquals(OrderItemType.CARRIAGE_BACK.getValue(), converter.convertToDatabaseColumn(OrderItemType.CARRIAGE_BACK));
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
		assertEquals(OrderItemType.CARRIAGE_GOING, converter.convertToEntityAttribute(OrderItemType.CARRIAGE_GOING.getValue()));
		assertEquals(OrderItemType.CARRIAGE_BACK, converter.convertToEntityAttribute(OrderItemType.CARRIAGE_BACK.getValue()));
	}

	@Test
	public void convertToEntityAttribute_WithInvalidValue_ReturnsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("Invalid"));
		assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute(null));
	}

}