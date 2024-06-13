package com.ferme.itservices.api.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Generated;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Generated
public abstract class JsonDataRead {
	public static <T> List<T> readJsonData(String filePath, TypeReference<List<T>> typeReference) {
		ObjectMapper objectMapper = new ObjectMapper();
		File path = new File(filePath);

		try {
			return objectMapper.readValue(path, typeReference);
		} catch (IOException e) {
			System.err.println("Error when reading JSON array: " + e.getMessage());
			return List.of();
		}
	}
}
