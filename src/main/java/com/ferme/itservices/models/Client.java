package com.ferme.itservices.models;

import com.ferme.itservicesspring.api.models.ids.ClientId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_client")
@IdClass(ClientId.class)
public class Client {
    @Id
    @NotBlank
    @Length(max = 40)
    @Column(length = 40, nullable = false)
    private String name;

    @Id
    @NotBlank
    @Pattern(regexp = "^(1[1-9]|2[1-9]|3\\d|4[1-9]|5[1-5]|6[1-9]|7[1-9]|8[1-9]|9[1-9])9[2-9]\\d{7}$")
    @Length(min = 8, max = 11)
    @Column(length = 11, nullable = false)
    private String phoneNumber;

    @Length(max = 30)
    @Column(length = 30)
    private String neighborhood;

    @Length(max = 30)
    @Column(length = 30)
    private String obs;
}