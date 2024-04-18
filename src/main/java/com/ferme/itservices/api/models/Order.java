package com.ferme.itservices.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferme.itservices.api.utils.models.Timestamps;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "`order`")
public class Order implements Serializable {
    @Id
    @JsonProperty("_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @NotEmpty
    @Size(max = 95)
    @Column(length = 95, nullable = false, updatable = false)
    private String header = "ORÇAMENTOS DE SERVIÇOS DE TERCEIROS - PESSOA FÍSICA, DESCRIÇÃO DE SERVIÇO(S) PRESTADO(S)";

    @NotBlank
    @Size(min = 4, max = 35, message = "Device name must be minimum 4 characters")
    @Column(name = "device_name", length = 35, nullable = false)
    private String deviceName;

    @Size(max = 35)
    @Column(name = "device_sn", unique = true, length = 35)
    private String deviceSN;

    @NotEmpty
    @Size(max = 250)
    @Column(length = 250)
    private String problems;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "rel_order_orderItems",
        joinColumns = {
            @JoinColumn(name = "order_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "orderItem_id",  referencedColumnName = "id")
        }
    )
    private List<OrderItem> orderItems = new ArrayList<>();

    @DecimalMin(value = "0.0", message = "Total price must be minimum 0.0")
    @DecimalMax(value = "9999.00", message = "Total price must be max 9999.00")
    @Column(length = 7, nullable = false)
    private Double totalPrice = 0.0;

    @Embedded
    @Valid
    @NotNull
    private Timestamps timestamps = new Timestamps();

    @PrePersist
    @PreUpdate
    private void calculateTotal() {
        if ((orderItems != null) && (!orderItems.isEmpty())) {
            this.totalPrice = orderItems.stream()
               .mapToDouble(OrderItem::getPrice)
               .sum();
        }
    }
}