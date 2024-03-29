package com.ferme.itservices.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferme.itservices.api.utils.models.Timestamps;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "client")
public class Client implements Serializable {
    @Id
    @JsonProperty("_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @NotBlank
    @Size(min = 4, max = 40, message = "Name must be minimum 10 characters")
    @Column(length = 40, nullable = false, updatable = false)
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\(?(\\d{2})\\)?[- ]?(\\d{4,5})[- ]?(\\d{4})$")
    @Size(min = 8, max = 11, message = "Phone number must be minimum 10 characters")
    @Column(name = "phone_number", length = 11, nullable = false, updatable = false)
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

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Embedded
    @Valid
    @NotNull
    private Timestamps timestamps = new Timestamps();
}