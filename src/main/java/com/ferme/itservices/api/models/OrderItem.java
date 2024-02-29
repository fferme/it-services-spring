package com.ferme.itservices.api.models;

import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_orderItem")
public class OrderItem extends BaseEntity implements Serializable {

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
}