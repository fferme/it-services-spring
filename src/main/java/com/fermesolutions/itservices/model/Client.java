package com.fermesolutions.itservices.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import lombok.Data;

@Data
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("_id")
    private Long id;

    @NotBlank
    @NotNull
    @Length(min = 5, max = 40)
    @Column(length = 40, nullable = false)
    private String name;

    @NotNull
    @Column(length = 1, nullable = false)
    private Character gender;

    @NotNull
    @Length(max = 11)
    @Pattern(regexp = "^[1-9][0-9]{9,10}$")
    @Column(length = 11, nullable = false)
    private String phoneNumber;

    @Length(max = 30)
    @Column(length = 30)
    private String district;

    @Length(max = 20)
    @Column(length = 20)
    private String reference;


}
