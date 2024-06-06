package com.ferme.itservices.application.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class OCRRestAPI {
	public static OCRRestAPI instance;

	private OCRRestAPI() { }

	public static OCRRestAPI getInstance() {
		return (instance == null)
			? instance = new OCRRestAPI()
			: instance;
	}

	public void convertToTXT(String filePath) {
		final String licenseCode = "7F050B24-570C-4DBB-A0A5-8A6BE632A5BD";
		final String userName = "fferme";
		final String language = "portuguese";
		final String outputFormat = "txt";

		String ocrURL = "http://www.ocrwebservice.com/restservices/processDocument" +
			"?gettext=true" +
			"&language=" + language +
			"&outputformat=" + outputFormat;

		try {
			byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

			final URI uri = new URI(ocrURL);
			final URL url = uri.toURL();

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Basic " +
				Base64.getEncoder().encodeToString((userName + ":" + licenseCode).getBytes()));
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(fileContent.length));

			OutputStream stream = connection.getOutputStream();
			stream.write(fileContent);
			stream.close();

			int httpCode = connection.getResponseCode();
			System.out.println("HTTP Response code: " + httpCode);

			if (httpCode == HttpURLConnection.HTTP_OK) {
				String jsonResponse = getResponseToString(connection.getInputStream());
				printOCRResponse(jsonResponse);
			} else if (httpCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				System.out.println("OCR Error Message: Unauthorizied request");
			} else {
				String jsonResponse = getResponseToString(connection.getErrorStream());

				JSONParser parser = new JSONParser();
				JSONObject jsonObj = (JSONObject) parser.parse(jsonResponse);

				System.out.println("Error Message: " + jsonObj.get("ErrorMessage"));
			}
			connection.disconnect();
		} catch (IOException | URISyntaxException | ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private static void printOCRResponse(String jsonResponse) throws ParseException, IOException, URISyntaxException {
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(jsonResponse);

			System.out.println("Available pages: " + jsonObj.get("AvailablePages"));

			JSONArray text = (JSONArray) jsonObj.get("OCRText");
			for (Object o : text) {
				System.out.println(" " + o);
			}

			String outputFileUrl = (String) jsonObj.get("OutputFileUrl");

			if ((outputFileUrl != null) && (!outputFileUrl.isEmpty())) {
				downloadConvertedFile(outputFileUrl);
			}
		} catch (IOException | URISyntaxException | ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private static void downloadConvertedFile(String outputFileUrl) throws IOException, URISyntaxException {
		final URI uri = new URI(outputFileUrl);
		final URL url = uri.toURL();
		HttpURLConnection downloadConnection = (HttpURLConnection) url.openConnection();

		if (downloadConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = downloadConnection.getInputStream();

			FileOutputStream outputStream = new FileOutputStream("./converted_file.txt");

			int bytesRead = -1;
			byte[] buffer = new byte[4096];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();
		}
		downloadConnection.disconnect();
	}

	private static String getResponseToString(InputStream inputStream) throws IOException {
		InputStreamReader responseStream = new InputStreamReader(inputStream);

		BufferedReader br = new BufferedReader(responseStream);
		StringBuffer strBuff = new StringBuffer();
		String s;
		while ((s = br.readLine()) != null) {
			strBuff.append(s);
		}

		return strBuff.toString();
	}

}
