package com.ferme.itservices.api.entities.models;

import com.ferme.itservices.api.application.utils.Price;
import com.ferme.itservices.security.auditing.models.AuditInfo;
import com.ferme.itservices.security.auditing.services.ApplicationAuditAware;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Types;
import java.time.LocalDateTime;
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
@EntityListeners(AuditingEntityListener.class)
public class Order implements Serializable {
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JdbcTypeCode(Types.VARCHAR)
	@Column(name = "id", updatable = false, unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
	private UUID id;

	@Size(max = 90)
	@Column(length = 90, updatable = false)
	private final String header = "ORÇAMENTOS DE SERVIÇOS DE TERCEIROS - PESSOA FÍSICA, DESCRIÇÃO DE SERVIÇO(S) PRESTADO(S)";

	@Setter
	@Size(max = 90, message = "Device name must be maximum 35 characters")
	@Column(name = "device_name", length = 90)
	private String deviceName;

	@Setter
	@Size(max = 35)
	@Column(name = "device_sn", unique = true, length = 35)
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
	@Builder.Default
	@DecimalMin(value = "0.0", message = "Total price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Total price must be max 9999.00")
	@Column(length = 7, nullable = false, updatable = false)
	private Double totalPrice = 0.0;

	@Embedded
	AuditInfo auditInfo;

	@PrePersist
	private void prePersist() {
		if (auditInfo == null) {
			auditInfo = new AuditInfo();
			ApplicationAuditAware applicationAuditAware = new ApplicationAuditAware();

			auditInfo.setCreatedBy(applicationAuditAware.getCurrentAuditor().orElse("System"));
			auditInfo.setUpdatedAt(null);
			auditInfo.setUpdatedBy(null);
		}

		Price priceInstance = Price.getInstance();
		final Double price = priceInstance.calculateTotalPrice(this.getOrderItems());

		if (price >= 0) { this.totalPrice = price; }
	}


	@PreUpdate
	private void preUpdate() {
		if (auditInfo == null) {
			auditInfo = new AuditInfo();
			ApplicationAuditAware applicationAuditAware = new ApplicationAuditAware();

			auditInfo.setUpdatedAt(LocalDateTime.now());
			auditInfo.setUpdatedBy(applicationAuditAware.getCurrentAuditor().orElse("System"));
		}

		Price priceInstance = Price.getInstance();
		final Double price = priceInstance.calculateTotalPrice(this.getOrderItems());

		if (price >= 0) { this.totalPrice = price; }
	}
}