package com.ferme.itservices.api.exceptions;

import java.io.Serializable;
import java.util.UUID;

public class RecordNotFoundException extends RuntimeException implements Serializable {
    public RecordNotFoundException(Class<?> clazz, UUID id) {
        super("Record not found in class " + clazz.getSimpleName() + " with id: " + id);
    }

}