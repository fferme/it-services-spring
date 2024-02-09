package com.ferme.itservices.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_orderItem")
public class OrderItem extends BaseEntity implements Serializable {

    private String orderItemType;

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

    @NotNull
    @Column(length = 5, nullable = false)
    private Boolean isPayed = false;
}