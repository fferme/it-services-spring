package com.ferme.itservices.application.exceptions;

import lombok.Generated;

import java.io.Serializable;

@Generated
public class RecordNotFoundException extends RuntimeException implements Serializable {
	public RecordNotFoundException(Class<?> clazz, String id) {
		super("Record not found in class " + clazz.getSimpleName() + " with id: " + id);
	}

}