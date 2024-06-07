package com.ferme.itservices.ocrreader.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FileDownloader {
	public void downloadConvertedFile(String outputFileUrl) {
		try {
			URI uri = new URI(outputFileUrl);
			URL url = uri.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				try (InputStream inputStream = connection.getInputStream();
				     FileOutputStream outputStream = new FileOutputStream("converted_file.txt")) {

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}

					System.out.println("Arquivo convertido baixado com sucesso.");
				}
			} else {
				System.out.println("Falha ao baixar o arquivo convertido. Código de resposta: " + connection.getResponseCode());
			}

			connection.disconnect();
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
