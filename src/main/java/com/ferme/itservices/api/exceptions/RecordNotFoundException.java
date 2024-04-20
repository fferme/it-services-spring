package com.ferme.itservices.api.exceptions;

import java.io.Serializable;

public class RecordNotFoundException extends RuntimeException implements Serializable {
	public RecordNotFoundException(Class<?> clazz, String id) {
		super("Record not found in class " + clazz.getSimpleName() + " with id: " + id);
	}

}