package com.fermesolutions.itservices.service.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg, Object id) {
        super(msg + id);
    }
}
