package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.utils.models.Timestamps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ClientConstants {
    private final String dateString = "2024-03-19 22:00:00";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static Date date;

    static {
        try {
            date = dateFormat.parse("2024-03-19 22:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Client CLIENT = new Client(
        UUID.randomUUID(),
        "José Pereira",
        "21980192888",
        "Tijuca",
        "Rua Silva Perez 39/21",
        "Amigo do Jaca",
        null,
        new Timestamps(date, date)
    );
    public static final Client INVALID_CLIENT = new Client(UUID.randomUUID(), "", "", "", "", "", null, null);

    public ClientConstants() throws ParseException { }
}