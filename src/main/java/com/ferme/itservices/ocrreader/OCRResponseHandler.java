package com.ferme.itservices.ocrreader;


import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Slf4j
public class OCRResponseHandler {
	public void printOCRResponse(String jsonResponse) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(jsonResponse);

			// Prints extracted OCR text
			log.info("Extracted test: ");
			JSONArray textArray = (JSONArray) jsonObj.get("OCRText");
			for (Object o : textArray) { log.info(o.toString()); }

			// If there's an output file, print it
			String outputFileUrl = (String) jsonObj.get("OutputFileUrl");
			if (outputFileUrl != null && !outputFileUrl.isEmpty()) {
				log.info("Output file URL: {}", outputFileUrl);
			}
		} catch (ParseException e) {
			log.error("Error analising JSON response: {}", e.getMessage());
		}
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
