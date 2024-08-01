package com.ferme.itservices.api.orderItem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ferme.itservices.api.order.models.Order;
import com.ferme.itservices.api.orderItem.enums.OrderItemType;
import com.ferme.itservices.api.orderItem.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.security.auditing.models.AuditInfo;
import com.ferme.itservices.security.auditing.services.ApplicationAuditAware;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
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
	@Size(max = 240)
	@Column(length = 240)
	private String description;

	@Setter
	@DecimalMin(value = "0.0", message = "Price must be minimum 0.0")
	@DecimalMax(value = "9999.00", message = "Price must be max 9999.00")
	@Column(length = 7, nullable = false)
	private Double price;

	@Setter
	@NotNull
	@Builder.Default
	private Boolean showInListAll = true;

	@Setter
	@Positive
	@Builder.Default
	private Integer quantity = 1;

	@JsonIgnore
	@ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
	private List<Order> orders;

	@Embedded
	private AuditInfo auditInfo;

	@PrePersist
	public void prePersist() {
		if (auditInfo == null) { auditInfo = new AuditInfo(); }
		ApplicationAuditAware applicationAuditAware = new ApplicationAuditAware();

		auditInfo.setCreatedBy(applicationAuditAware.getCurrentAuditor().orElse("System"));
	}

	@PreUpdate
	public void preUpdate() {
		if (auditInfo == null) { auditInfo = new AuditInfo(); }
		ApplicationAuditAware applicationAuditAware = new ApplicationAuditAware();

		auditInfo.setUpdatedBy(applicationAuditAware.getCurrentAuditor().orElse("System"));
	}
}