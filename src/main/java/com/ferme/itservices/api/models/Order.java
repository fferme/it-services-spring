package com.ferme.itservices.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "`order`")
public class Order extends BaseEntity implements Serializable {
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

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