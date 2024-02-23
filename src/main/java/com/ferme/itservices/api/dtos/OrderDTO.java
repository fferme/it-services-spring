package com.ferme.itservices.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder
public class OrderDTO extends BaseEntityDTO implements Serializable {
    @NotEmpty
    @Size(max = 95)
    private final String header;

    @NotBlank
    @Size(min = 4, max = 35, message = "Device name must be minimum 4 characters")
    private String deviceName;

    @NotBlank
    @Size(min = 4, max = 35, message = "Device serial number name must be minimum 4 characters")
    private String deviceSN;

    @NotEmpty
    @Size(max = 250)
    private String problems;
}