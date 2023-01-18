package com.fermesolutions.itservices.model;

import lombok.Data;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fermesolutions.itservices.model.enums.OrderItemType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "tb_orderitem")
public class OrderItem {
    @Id
    @JsonProperty(value = "_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    
    @NotBlank
    @Column(length = 15, nullable = false)
    private OrderItemType orderItemType;

    @NotBlank
    @Length(max = 80)
    @Column(length = 80, nullable = false)
    private String description;
    
    @NotBlank
    @DecimalMin(value = "0.0")
    private Double price;
}
