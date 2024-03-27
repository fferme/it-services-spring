package com.ferme.itservices.common;

import com.ferme.itservices.api.models.Client;
import com.ferme.itservices.api.utils.models.DateUtils;
import com.ferme.itservices.api.utils.models.Timestamps;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

public class ClientConstants {
    private static final Date date = DateUtils.parseDate("2024-03-19 22:00:00");
    private static final Timestamps timestamps = new Timestamps(date, date);

    public static final Client CLIENT = new Client(
        UUID.randomUUID(),
        "José Pereira",
        "21980192888",
        "Tijuca",
        "Rua Silva Perez 39/21",
        "Amigo do Jaca",
        null,
        timestamps
    );

    public static final Client INVALID_CLIENT = new Client(UUID.randomUUID(), "", "", "", "", "", null, null);

    public ClientConstants() throws ParseException { }
}