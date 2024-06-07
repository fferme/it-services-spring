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

				try (InputStream inputStream = downloadConnection.getInputStream()) {
					Path destinationPath = Paths.get(destinationFilePath);
					Files.copy(inputStream, destinationPath);
					log.info("File conversion completed successfully");
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
