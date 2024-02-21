package com.ferme.itservices.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO extends BaseEntityDTO implements Serializable {
    private String header;
    private String deviceName;
    private String deviceSN;
    private String problems;
    private List<OrderItemDTO> orderItems;
}