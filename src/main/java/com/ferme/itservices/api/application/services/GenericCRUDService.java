package com.ferme.itservices.api.application.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface GenericCRUDService<T, UUID> {
	List<T> listAll();

	T findById(@NotNull UUID id);

	T create(T entity);

	T update(@NotNull UUID id, @Valid @NotNull T entity);

	void deleteById(@NotNull UUID id);

	void deleteAll();
}
