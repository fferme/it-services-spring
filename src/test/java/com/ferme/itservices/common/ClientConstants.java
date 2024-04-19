package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.utils.models.Timestamps;

public class ClientConstants {
    public static final Client VALID_CLIENT = Client.builder()
       .name("José Pereira")
       .phoneNumber("21986861613")
       .neighborhood("Tijuca")
       .address("Rua Silva Perez 39/21")
       .reference("Amigo do Jaca")
       .timestamps(new Timestamps())
       .build();

    public static final Client INVALID_CLIENT = Client.builder()
       .name("")
       .phoneNumber("")
       .neighborhood("")
       .address("")
       .reference("")
       .build();
}