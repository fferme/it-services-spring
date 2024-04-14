package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Client;

public class ClientConstants {
    public static final Client VALID_CLIENT = Client.builder()
       .name("José Pereira")
       .phoneNumber("21980192888")
       .neighborhood("Tijuca")
       .address("Rua Silva Perez 39/21")
       .reference("Amigo do Jaca")
       .build();

    public static final Client INVALID_CLIENT = Client.builder()
       .name("")
       .phoneNumber("")
       .neighborhood("")
       .address("")
       .reference("")
       .build();
}