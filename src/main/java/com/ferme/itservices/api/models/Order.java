package com.ferme.itservices.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "device_sn", length = 35)
    private String deviceSN;

    @NotEmpty
    @Size(max = 250)
    @Column(length = 250)
    private String problems;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        return this.getId() != null && this.getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}