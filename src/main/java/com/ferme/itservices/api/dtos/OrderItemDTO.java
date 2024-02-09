package com.ferme.itservices.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO extends BaseEntityDTO implements Serializable {
    private String orderItemType;
    private String description;
    private Double cashPrice;
    private Double installmentPrice;
    private Boolean isPayed = false;
}