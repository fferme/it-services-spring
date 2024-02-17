package com.ferme.itservices.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    @ElementCollection
    @Size(max = 250)
    @Column(length = 250, nullable = false, updatable = false)
    private final List<String> header = new ArrayList<>(Arrays.asList("ORÇAMENTOS DE SERVIÇOS DE TERCEIROS - PESSOA FÍSICA", "DESCRIÇÃO DE SERVIÇO(S) PRESTADO(S)"));

    @NotBlank
    @Size(min = 4, max = 35, message = "Device name must be minimum 4 characters")
    @Column(length = 35, nullable = false, updatable = false)
    private String deviceName;

    @NotNull
    @Min(value = 0L, message = "Ad ID must be minimum 9 characters")
    @Max(value = 99999999999L, message = "Ad ID must be maximum 11 characters")
    @Column(length = 11, nullable = false, updatable = false)
    private Integer adId;

    @NotEmpty
    @ElementCollection
    @Size(max = 250)
    @Column(length = 250)
    private List<String> problems = new ArrayList<>();
}