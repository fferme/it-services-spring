package com.ferme.itservices.api.exceptions;

import java.io.Serializable;

public class RecordAlreadyExistsException extends RuntimeException implements Serializable {
	public RecordAlreadyExistsException(Class<?> clazz, String id) {
		super("Record already exists in class " + clazz.getSimpleName() + " with id: " + id);
	}

}