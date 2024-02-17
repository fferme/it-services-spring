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
    private List<String> header;
    private String deviceName;
    private Integer adId;
    private List<String> problems;
}