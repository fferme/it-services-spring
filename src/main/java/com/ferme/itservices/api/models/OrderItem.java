package com.ferme.itservices.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orderItem")
public class OrderItem implements Serializable {
    @Id
    @JsonProperty("_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @NotNull
    @Convert(converter = OrderItemTypeConverter.class)
    @Column(length = 30, nullable = false)
    private OrderItemType orderItemType;

    @NotBlank
    @Size(min = 3, max = 70, message = "Description must be minimum 5 characters")
    @Column(length = 70, nullable = false)
    private String description;

    @DecimalMin(value = "0.0", message = "Cash price must be minimum 0.0")
    @DecimalMax(value = "9999.00", message = "Cash price must be max 9999.00")
    @Column(length = 7, nullable = false)
    private Double cashPrice;

    @DecimalMin(value = "0.0", message = "Installment price must be minimum 0.0")
    @DecimalMax(value = "9999.00", message = "Installment price must be max 9999.00")
    @Column(length = 7, nullable = false)
    private Double installmentPrice;

    @ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    @Column(nullable = false)
    private Date updatedAt;

    @PrePersist
    private void onCreate() {
        this.setCreatedAt(Date.from(Instant.now()));
        this.setUpdatedAt(Date.from(Instant.now()));
    }

    @PreUpdate
    private void onUpdate() {
        this.setUpdatedAt(Date.from(Instant.now()));
    }
}