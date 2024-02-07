package com.ferme.itservices.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_client")
public class Client extends BaseEntity implements Serializable {
    @NotBlank
    @Size(min = 4, max = 40, message = "Name must be minimum 10 characters")
    @Column(length = 40, nullable = false, updatable = false)
    private String name;

    @NotBlank
    @Pattern(regexp = "^(1[1-9]|2[1-9]|3\\d|4[1-9]|5[1-5]|6[1-9]|7[1-9]|8[1-9]|9[1-9])9[2-9]\\d{7}$")
    @Size(min = 8, max = 11, message = "Phone number must be minimum 10 characters")
    @Column(length = 11, nullable = false, updatable = false)
    private String phoneNumber;

    @Size(max = 30)
    @Column(length = 30)
    private String neighborhood;

    @Size(max = 30)
    @Column(length = 30)
    private String reference;

    @PrePersist
    private void onCreation() {
        this.setCreatedAt(new Date());
        this.setUpdatedAt(new Date());
    }

    @PreUpdate
    private void onUpdate() {
        this.setUpdatedAt(new Date());
    }
}