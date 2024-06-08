package com.ferme.itservices.ocrreader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferme.itservices.ocrreader.file.FileConverter;
import com.ferme.itservices.ocrreader.file.FileDownloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.ferme.itservices.ocrreader.file.FileUtils.getClientPath;

@Slf4j
public class OCRRestAPI {
	private static volatile OCRRestAPI instance;

	private final FileConverter fileConverter;
	private final OCRResponseHandler ocrResponseHandler = OCRResponseHandler.getInstance();
	private final FileDownloader fileDownloader = FileDownloader.getInstance();
	private final ObjectMapper objectMapper;

	private OCRRestAPI(FileConverter fileConverter, ObjectMapper objectMapper) {
		this.fileConverter = fileConverter;
		this.objectMapper = objectMapper;
	}

	public static OCRRestAPI getInstance(FileConverter fileConverter, ObjectMapper objectMapper) {
		if (instance == null) {
			synchronized (OCRRestAPI.class) {
				if (instance == null) {
					instance = new OCRRestAPI(fileConverter, objectMapper);
				}
			}
		}
		return instance;
	}

	public void iterateOverPhotos(String photosDir) {
		Path dirPath = Paths.get(photosDir);
		if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.jpg")) {
				for (Path entry : stream) {
					extractTextFromJPG(entry.toAbsolutePath().toString());
				}
			} catch (IOException e) {
				log.error("Error accessing folder: {}", e.getMessage());
			}
		} else {
			log.error("The specified folder does not exist or is not a directory");
		}
	}

	public void extractTextFromJPG(String filePath) {
		log.info(filePath);
		log.info(getClientPath(filePath));
		try {
			HttpURLConnection connection = fileConverter.sendFile(filePath);
			int httpCode = connection.getResponseCode();
			log.info("{}({})", httpCode, connection.getResponseMessage());

			if (httpCode == HttpURLConnection.HTTP_OK) {
				assert objectMapper != null;
				JsonNode jsonObj = objectMapper.readTree(fileConverter.getResponseToString(connection.getInputStream()));
				log.info("Pages: {}", jsonObj.get("AvailablePages").asText());

				JsonNode outputFileUrlNode = jsonObj.get("OutputFileUrl");
				if ((outputFileUrlNode != null) && (!outputFileUrlNode.asText().isEmpty())) {
					fileDownloader.downloadFile(outputFileUrlNode.asText(), "src/main/resources/output/" +
						getClientPath(filePath));
				}

			} else if (httpCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				log.error("{}: Error: {}", httpCode, connection.getResponseMessage());
			} else {
				String jsonResponse = fileConverter.getResponseToString(connection.getErrorStream());
				ocrResponseHandler.handleErrorResponse(jsonResponse);
			}
			connection.disconnect();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
