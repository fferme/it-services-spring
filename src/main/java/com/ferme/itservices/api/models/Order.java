package com.ferme.itservices.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tb_order")
public class Order extends BaseEntity implements Serializable {
    @NotEmpty
    @Size(max = 95)
    @Column(length = 95, nullable = false, updatable = false)
    private String header = "ORÇAMENTOS DE SERVIÇOS DE TERCEIROS - PESSOA FÍSICA, DESCRIÇÃO DE SERVIÇO(S) PRESTADO(S)";

    @NotBlank
    @Size(min = 4, max = 35, message = "Device name must be minimum 4 characters")
    @Column(length = 35, nullable = false)
    private String deviceName;

    @NotBlank
    @Size(min = 4, max = 35, message = "Device serial number name must be minimum 4 characters")
    @Column(length = 35, nullable = false)
    private String deviceSN;

    @NotEmpty
    @Size(max = 250)
    @Column(length = 250)
    private String problems;

//    @OneToMany(targetEntity = OrderItem.class, cascade = CascadeType.ALL)
//    @JoinColumn(name = "ooi_fk", referencedColumnName = "id")
//    private List<OrderItem> orderItems;
}