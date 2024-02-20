package com.ferme.itservices.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_order")
public class Order extends BaseEntity implements Serializable {

    @NotEmpty
    @Size(max = 95)
    @Column(length = 95, nullable = false, updatable = false)
    private final String header = "ORÇAMENTOS DE SERVIÇOS DE TERCEIROS - PESSOA FÍSICA, DESCRIÇÃO DE SERVIÇO(S) PRESTADO(S)";

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
}