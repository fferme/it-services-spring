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
public class ClientDTO extends BaseEntityDTO implements Serializable {
    private String name;
    private String phoneNumber;
    private String neighborhood;
    private String reference;
}