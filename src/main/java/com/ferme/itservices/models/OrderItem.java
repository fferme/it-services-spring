package com.ferme.itservices.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.enums.converter.OrderItemTypeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity(name = "orderItems")
@Table(name = "order_Items")
public class OrderItem implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, unique = true, nullable = false)
	private UUID id;

	@Setter
	@NotNull
	@Convert(converter = OrderItemTypeConverter.class)
	@Column(length = 30, nullable = false)
	private OrderItemType orderItemType;

	@Setter
	@Size(max = 70)
	@Column(length = 70)
	private String description;

	@Setter
	@DecimalMin(value = "0.0", message = "Price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Price must be max 9999.00")
	@Column(length = 7, nullable = false)
	private Double price;

	@JsonIgnore
	@ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
	private List<Order> orders;
}