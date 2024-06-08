package com.ferme.itservices.ocrreader.file;

import com.ferme.itservices.ocrreader.OCRService;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@AllArgsConstructor
public class FileConverter {
	private final OCRService ocrService;

	public HttpURLConnection sendFile(String filePath) throws IOException {
		final String licenseCode = "93C9860A-8B95-46A4-80E0-A81DC205A4C7";
		final String username = "FFERME1212";
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

		OutputStream stream = connection.getOutputStream();
		stream.write(fileContent);
		stream.close();

		return connection;
	}

	public String getResponseToString(InputStream inputStream) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			StringBuilder strBuff = new StringBuilder();
			char[] buffer = new char[8192];
			int length;

			while ((length = br.read(buffer)) != -1) {
				strBuff.append(buffer, 0, length);
			}

			return strBuff.toString();
		}
	}
}
