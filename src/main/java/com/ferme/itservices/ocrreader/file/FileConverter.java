package com.ferme.itservices.ocrreader.file;

import com.ferme.itservices.ocrreader.OCRService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FileConverter {
	private final OCRService ocrService;

	public FileConverter(OCRService ocrService) {
		this.ocrService = ocrService;
	}

	public HttpURLConnection sendFile(String filePath) throws IOException {
		final String licenseCode = "7F050B24-570C-4DBB-A0A5-8A6BE632A5BD";
		final String username = "FFERME";
		final String language = "portuguese";
		final String outputFormat = "txt";

		byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
		HttpURLConnection connection = ocrService.connectToOCRService("http://www.ocrwebservice.com/restservices/processDocument" +
			                                                              "?gettext=true" +
			                                                              "&language=" + language +
			                                                              "&outputformat=" + outputFormat);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + licenseCode).getBytes()));
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(fileContent.length));

		try (OutputStream outputStream = connection.getOutputStream()) {
			outputStream.write(fileContent);
		}

		return connection;
	}

	public String getResponseToString(InputStream inputStream) throws IOException {
		StringBuilder response = new StringBuilder();
		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = inputStream.read(buffer)) != -1) {
			response.append(new String(buffer, 0, bytesRead));
		}

		return response.toString();
	}
}
