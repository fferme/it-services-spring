package com.ferme.itservices.ocrreader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Getter
@Setter
@AllArgsConstructor
public class OCRService {
	public HttpURLConnection connectToOCRService(String ocrURL) {
		try {
			URI uri = new URI(ocrURL);
			URL url = uri.toURL();

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");

			return connection;
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
