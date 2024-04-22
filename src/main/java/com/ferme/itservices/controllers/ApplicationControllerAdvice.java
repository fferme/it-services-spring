package com.ferme.itservices.controllers;

import com.ferme.itservices.exceptions.RecordAlreadyExistsException;
import com.ferme.itservices.exceptions.RecordNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RecordNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotFoundException(RecordNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(RecordAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleAlreadyExistsException(RecordAlreadyExistsException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleConstraintViolationException(ConstraintViolationException ex) {
		return ex.getConstraintViolations().stream()
			.map(error -> error.getPropertyPath() + " " + error.getMessage())
			.reduce("", (acc, error) -> acc + error + "\n");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		if (ex != null && ex.getRequiredType() != null) {
			String type = ex.getRequiredType().getName();
			String[] typeParts = type.split("\\.");
			String typeName = typeParts[typeParts.length - 1];
			return ex.getName() + " should be of type " + typeName;
		}
		return "Argument type not valid";
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return super.handleMethodArgumentNotValid(ex, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	private String handleConflict(DataIntegrityViolationException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	private ResponseEntity<Object> handleBadRequest(EmptyResultDataAccessException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}