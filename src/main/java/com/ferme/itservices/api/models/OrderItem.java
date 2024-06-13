package com.ferme.itservices.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.sql.Types;
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
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JdbcTypeCode(Types.VARCHAR)
	@Column(name = "id", updatable = false, unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
	private UUID id;

	@Setter
	@NotNull
	@Convert(converter = OrderItemTypeConverter.class)
	@Column(length = 30, nullable = false)
	private OrderItemType orderItemType;

	@Setter
	@Size(max = 50)
	@Column(length = 50, unique = true)
	private String description;

	@Setter
	@DecimalMin(value = "0.0", message = "Price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Price must be max 9999.00")
	@Column(length = 7, nullable = false)
	private Double price;

	@Setter
	@NotNull
	private Boolean showInListAll = true;

	@JsonIgnore
	@ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
	private List<Order> orders;
}