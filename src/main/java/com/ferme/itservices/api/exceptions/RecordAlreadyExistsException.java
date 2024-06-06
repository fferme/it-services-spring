package com.ferme.itservices.api.exceptions;

import lombok.Generated;

import java.io.Serializable;

@Generated
public class RecordAlreadyExistsException extends RuntimeException implements Serializable {
	public RecordAlreadyExistsException(Class<?> clazz, String id) {
		super("Record already exists in class " + clazz.getSimpleName() + " with id: " + id);
	}

}