package com.ferme.itservices.ocrreader;


import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Slf4j
public class OCRResponseHandler {
	public static OCRResponseHandler instance;

	private OCRResponseHandler() { }

	public static OCRResponseHandler getInstance() {
		return (instance == null)
			? instance = new OCRResponseHandler()
			: instance;
	}

	public void handleErrorResponse(String jsonResponse) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(jsonResponse);

			String errorMessage = (String) jsonObj.get("ErrorMessage");
			log.error("Error message: {}", errorMessage);
		} catch (ParseException e) {
			log.error("JSON parse exception: {}", e.getMessage());
		}
	}
}
