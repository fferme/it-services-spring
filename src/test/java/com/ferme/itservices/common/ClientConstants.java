package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Client;

public class ClientConstants {
    public static final Client CLIENT = new Client("José Alves", "21983904857", "Tijuca", "Av. Maracanã 74", "Cliente");
    public static final Client INVALID_CLIENT = new Client("", "", "", "", "");
}