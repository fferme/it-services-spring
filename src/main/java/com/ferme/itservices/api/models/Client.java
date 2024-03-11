package com.ferme.itservices.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "client")
public class Client extends BaseEntity implements Serializable {
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

    public void addOrder(Order order) {
        orders.add(order);
        order.setClient(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setClient(null);
    }

    @Override
    public boolean equals(Object obj) {
       return EqualsBuilder.reflectionEquals(obj, this);
    }
}