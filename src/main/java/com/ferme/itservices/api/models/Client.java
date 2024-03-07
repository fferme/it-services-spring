package com.ferme.itservices.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "clients")
public class Client extends BaseEntity implements Serializable {
    @NotBlank
    @Size(min = 4, max = 40, message = "Name must be minimum 10 characters")
    @Column(length = 40, nullable = false, updatable = false)
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\(?(\\d{2})\\)?[- ]?(\\d{4,5})[- ]?(\\d{4})$")
    @Size(min = 8, max = 11, message = "Phone number must be minimum 10 characters")
    @Column(length = 11, nullable = false, updatable = false)
    private String phoneNumber;

    @Size(max = 20)
    @Column(length = 20)
    private String neighborhood;

    @Size(max = 50)
    @Column(length = 50)
    private String address;

    @Size(max = 70)
    @Column(length = 70)
    private String reference;

    @Override
    public boolean equals(Object obj) {
       return EqualsBuilder.reflectionEquals(obj, this);
    }
}