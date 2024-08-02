package com.ferme.itservices.api.application.services;

import java.util.List;

public interface GenericCRUDService<T, ID> {
	List<T> listAll();

	T findById(ID id);

	T create(T entity);

	T update(ID id, T entity);

	void deleteById(ID id);

	void deleteAll();
}
