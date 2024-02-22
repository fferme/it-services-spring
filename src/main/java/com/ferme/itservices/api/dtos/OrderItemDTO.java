package com.ferme.itservices.api.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO extends BaseEntityDTO implements Serializable {
    @NotNull
    private String orderItemType;
    @NotBlank
    private String description;

    @DecimalMin(value = "0.0", message = "Cash price must be minimum 0.0")
    @DecimalMax(value = "9999.00", message = "Cash price must be max 9999.00")
    private Double cashPrice;

    @DecimalMin(value = "0.0", message = "Installment price must be minimum 0.0")
    @DecimalMax(value = "9999.00", message = "Installment price must be max 9999.00")
    private Double installmentPrice;
}