package com.ferme.itservices.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferme.itservices.api.enums.OrderItemType;
import com.ferme.itservices.api.enums.converter.OrderItemTypeConverter;
import com.ferme.itservices.api.utils.models.Timestamps;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orderItem")
public class OrderItem implements Serializable {
    @Id
    @JsonProperty("_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @NotNull
    @Convert(converter = OrderItemTypeConverter.class)
    @Column(length = 30, nullable = false)
    private OrderItemType orderItemType;

    @NotBlank
    @Size(min = 3, max = 70, message = "Description must be minimum 5 characters")
    @Column(length = 70, nullable = false)
    private String description;

    @DecimalMin(value = "0.0", message = "Price must be minimum 0.0")
    @DecimalMax(value = "9999.00", message = "Price must be max 9999.00")
    @Column(length = 7, nullable = false)
    private Double price;

    @ManyToMany(mappedBy = "orderItems", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @Embedded
    @Valid
    @NotNull
    private Timestamps timestamps = new Timestamps();
}