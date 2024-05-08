package com.ferme.itservices.models;

import com.ferme.itservices.enums.OrderItemType;
import com.ferme.itservices.enums.converter.OrderItemTypeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "orderItems")
@Table(name = "orderItems")
public class OrderItem implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, unique = true, nullable = false)
	private Long id;

	@NotNull
	@Convert(converter = OrderItemTypeConverter.class)
	@Column(length = 30, nullable = false)
	private OrderItemType orderItemType;

	@Size(max = 70)
	@Column(length = 70)
	private String description;

	@DecimalMin(value = "0.0", message = "Price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Price must be max 9999.00")
	@Column(length = 7, nullable = false)
	private Double price;

//	@JsonIgnore
//	@ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
//	private List<Order> orders;
}