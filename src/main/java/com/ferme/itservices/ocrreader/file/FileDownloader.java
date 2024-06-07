package com.ferme.itservices.ocrreader.file;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileDownloader {
	public static FileDownloader instance;

	public static FileDownloader getInstance() {
		return (instance == null)
			? instance = new FileDownloader()
			: instance;
	}

	public void downloadFile(String outputFileUrl, String destinationFilePath) {
		try {
			URI uri = new URI(outputFileUrl);
			URL url = uri.toURL();
			HttpURLConnection downloadConnection = (HttpURLConnection) url.openConnection();

			if (downloadConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				Path destinationPath = Paths.get(destinationFilePath);

				// Check if the file already exists
				if (Files.exists(destinationPath)) {
					Files.delete(destinationPath);
					log.info("Existing file deleted: {}", destinationFilePath);
				}

				// Download and save the file
				try (InputStream inputStream = downloadConnection.getInputStream()) {
					Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
					log.info("File downloaded and saved successfully to: {}", destinationFilePath);
				}
			} else {
				log.error("Fail to download converted file. Status code: {}", downloadConnection.getResponseCode());
			}

			downloadConnection.disconnect();
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException("Failed to download the file", e);
		}
	}
}
