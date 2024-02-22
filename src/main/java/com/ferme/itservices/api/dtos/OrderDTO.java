package com.ferme.itservices.api.dtos;

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
    public String header;
    public String deviceName;
    public String deviceSN;
    public String problems;
    public List<OrderItemDTO> orderItems;
}