package com.ferme.itservices.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "orders")
@Table(name = "orders")
public class Order implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, unique = true, nullable = false)
	private Long id;

	@Size(max = 95)
	@Column(length = 95, updatable = false)
	private final String header = "ORÇAMENTOS DE SERVIÇOS DE TERCEIROS - PESSOA FÍSICA, DESCRIÇÃO DE SERVIÇO(S) PRESTADO(S)";

	@NotBlank
	@Size(min = 4, max = 35, message = "Device name must be minimum 4 characters")
	@Column(name = "device_name", length = 35, nullable = false)
	private String deviceName;

	@NotBlank
	@Size(max = 35)
	@Column(name = "device_sn", unique = true, nullable = false, length = 35)
	private String deviceSN;

	@Size(max = 250)
	@Column(length = 250)
	private String problems;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "client_id")
	private Client client;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(
		name = "rel_order_orderItems",
		joinColumns = {
			@JoinColumn(name = "order_id", referencedColumnName = "id")
		},
		inverseJoinColumns = {
			@JoinColumn(name = "orderItem_id", referencedColumnName = "id")
		}
	)
	private List<OrderItem> orderItems;

	@DecimalMin(value = "0.0", message = "Total price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Total price must be max 9999.00")
	@Column(length = 7, nullable = false)
	private Double totalPrice = 0.0;

	@PrePersist
	@PreUpdate
	private void calculateTotal() {
		if (!orderItems.isEmpty()) {
			this.totalPrice = orderItems.stream()
				.mapToDouble(OrderItem::getPrice)
				.sum();
		}
	}
}