package com.fermesolutions.itservices.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import lombok.Data;

@Data
@Entity
public class Client {
    @Id
    @JsonProperty("_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id = UUID.randomUUID(); 

    @NotBlank
    @Length(min = 5, max = 40)
    @Column(length = 40, nullable = false)
    private String name;

    @NotBlank
    @Column(length = 1, nullable = false)
    private Character gender;

    @NotNull
    @Length(max = 11)
    @Pattern(regexp = "^[1-9][0-9]{9,10}$")
    @Column(length = 11, nullable = false)
    private String phoneNumber;

    @Length(max = 30)
    @Column(length = 30)
    private String neighbourhood;

    @Length(max = 20)
    @Column(length = 20)
    private String reference;
}
