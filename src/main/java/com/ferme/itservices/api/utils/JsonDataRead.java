package com.ferme.itservices.api.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Generated
@Slf4j
public abstract class JsonDataRead {
	public static <T> List<T> readJsonData(String filePath, TypeReference<List<T>> typeReference) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File path = new File(filePath);

		try {
			return objectMapper.readValue(path, typeReference);
		} catch (IOException e) {
			log.error("Error when reading JSON array: {}", e.getMessage());
			return List.of();
		}
	}
}
