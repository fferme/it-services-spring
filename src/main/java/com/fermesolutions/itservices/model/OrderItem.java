package com.fermesolutions.itservices.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

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
import jakarta.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "tb_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("_id")
    private Long id;
    
    @NotNull
    @Column(length = 15, nullable = false)
    private OrderItemType orderItemType;

    @NotNull
    @Length(max = 80)
    @Column(length = 80, nullable = false)
    private String description;
    
    @NotNull
    @DecimalMin(value = "0.0")
    private Double price;

    public Object map(Object object) {
        return null;
    }

    //@ManyToOne
    //@JoinColumn(name = "order.id")
    //private Order order;
}
