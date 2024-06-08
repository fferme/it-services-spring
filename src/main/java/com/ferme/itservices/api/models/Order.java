package com.ferme.itservices.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ferme.itservices.api.utils.Price;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity(name = "orders")
@Table(name = "orders")
public class Order implements Serializable {
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JdbcTypeCode(Types.VARCHAR)
	@Column(name = "id", updatable = false, unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
	private UUID id;

	@Size(max = 95)
	@Column(length = 95, updatable = false, unique = true)
	private final String header = "ORÇAMENTOS DE SERVIÇOS DE TERCEIROS - PESSOA FÍSICA, DESCRIÇÃO DE SERVIÇO(S) PRESTADO(S)";

	@Setter
	@Size(max = 35, message = "Device name must be maximum 35 characters")
	@Column(name = "device_name", length = 35)
	private String deviceName;

	@Setter
	@NotBlank
	@Size(max = 35)
	@Column(name = "device_sn", unique = true, nullable = false, length = 35)
	private String deviceSN;

	@Setter
	@Size(max = 250)
	@Column(length = 250)
	private String issues;

	@Setter
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "client_id")
	private Client client;

	@Setter
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
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

	@Setter
	@DecimalMin(value = "0.0", message = "Total price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Total price must be max 9999.00")
	@Column(length = 7, nullable = false, updatable = false)
	private Double totalPrice = 0.0;

	@Setter
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-3")
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	private void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		}

		Price priceInstance = Price.getInstance();
		final Double price = priceInstance.calculateTotalPrice(this.getOrderItems());

		if (price >= 0) { this.totalPrice = price; }
	}


	@PreUpdate
	private void preUpdate() {
		Price priceInstance = Price.getInstance();
		final Double price = priceInstance.calculateTotalPrice(this.getOrderItems());

		if (price >= 0) { this.totalPrice = price; }
	}
}