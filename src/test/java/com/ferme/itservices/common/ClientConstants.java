package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Client;

public class ClientConstants {
    public static final Client CLIENT = Client.builder()
                                              .name("Jorge")
                                              .phoneNumber("21988011773")
                                              .address("Rua Aquidabã 74")
                                              .neighborhood("Méier")
                                              .reference("Eu")
                                              .build();
    public static final Client INVALID_CLIENT = Client.builder()
                                              .name("")
                                              .phoneNumber("")
                                              .address("")
                                              .neighborhood("")
                                              .reference("")
                                              .build();
}