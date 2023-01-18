package com.fermesolutions.itservices.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fermesolutions.itservices.model.enums.ComputerType;
import com.fermesolutions.itservices.model.enums.OSType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_computer")
public class Computer {
    @Id
    @JsonProperty("_idAD")
    private Long idAD;

    @NotNull
    @Column(length = 10, nullable = false)
    private ComputerType computerType;

    @NotNull
    @Column(length = 35, nullable = false)
    private OSType osType;

    @NotBlank
    @NotNull
    @Column(length = 40, nullable = false)
    private String cpu;

    @NotBlank
    @NotNull
    @Column(length = 40, nullable = false)
    private String ram;

    @NotBlank
    @NotNull
    @Column(length = 40, nullable = false)
    private String gpu;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
}
