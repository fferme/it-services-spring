package com.ferme.itservices.ocrreader;

import com.ferme.itservices.ocrreader.file.FileConverter;
import com.ferme.itservices.ocrreader.file.FileDownloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;

@Slf4j
public class OCRRestAPI {
	private static class InstanceHolder {
		private static final OCRRestAPI instance = new OCRRestAPI();
	}

	public static OCRRestAPI getInstance() {
		return InstanceHolder.instance;
	}

	//private static final Logger logger = LoggerFactory.getLogger(OCRRestAPI.class);

	private final FileConverter fileConverter;
	private final OCRResponseHandler ocrResponseHandler;

	private OCRRestAPI() {
		OCRService ocrService = new OCRService();
		this.fileConverter = new FileConverter(ocrService);
		this.ocrResponseHandler = new OCRResponseHandler();
		FileDownloader fileDownloader = new FileDownloader();
	}

	public void extractTextFromJPG(String filePath) {
		try {
			HttpURLConnection connection = fileConverter.sendFile(filePath);
			int httpCode = connection.getResponseCode();
			log.info("{}({})", httpCode, connection.getResponseMessage());

			if (httpCode == HttpURLConnection.HTTP_OK) {
				String jsonResponse = fileConverter.getResponseToString(connection.getInputStream());
				ocrResponseHandler.printOCRResponse(jsonResponse);
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
