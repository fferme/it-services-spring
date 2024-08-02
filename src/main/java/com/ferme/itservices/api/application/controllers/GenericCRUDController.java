package com.ferme.itservices.api.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GenericCRUDController<T, UUID> {
	ResponseEntity<List<T>> listAll();

	ResponseEntity<T> findById(@PathVariable UUID id);

	ResponseEntity<T> create(@RequestBody T dto);

	ResponseEntity<T> update(@PathVariable UUID id, @RequestBody T dto);

	ResponseEntity<Void> deleteById(@PathVariable UUID id);

	ResponseEntity<Void> deleteAll();
}
