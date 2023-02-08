package com.fermesolutions.itservices.dto;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fermesolutions.itservices.model.Order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClientDTO(
        @JsonProperty("_id") Long id,
        @NotBlank @NotNull @Length(min = 5, max = 40) String name,
        @NotNull Character gender, 
        @NotNull @Length(max = 11) @Pattern(regexp = "^[1-9][0-9]{9,10}$") String phoneNumber, 
        @Length(max = 30) String neighbourhood, 
        @Length(max = 20) String reference,
        @JsonIgnore Set<Order> orders
    ) {
}
