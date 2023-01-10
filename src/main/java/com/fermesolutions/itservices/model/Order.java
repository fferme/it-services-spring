package com.fermesolutions.itservices.model;

import lombok.Data;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tb_order")
public class Order {
    @Id
    @JsonProperty("_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id = UUID.randomUUID(); 

    @NotBlank
    @Length(max = 80)
    @Column(length = 80, nullable = false)
    private String issues;
    
    @NotBlank
    @DecimalMin(value = "0.0")
    private String notes;
}
