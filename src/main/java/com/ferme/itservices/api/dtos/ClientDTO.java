package com.ferme.itservices.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder
public class ClientDTO extends BaseEntityDTO implements Serializable {
    @NotBlank
    @Size(min = 4, max = 40, message = "Name must be minimum 10 characters")
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\(?(\\d{2})\\)?[- ]?(\\d{4,5})[- ]?(\\d{4})$")
    @Size(min = 8, max = 11, message = "Phone number must be minimum 10 characters")
    private String phoneNumber;

    @Size(max = 20)
    private String neighborhood;

    @Size(max = 50)
    private String address;

    @Size(max = 70)
    private String reference;
}