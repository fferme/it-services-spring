package com.ferme.itservices.ocrreader.file;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class FileUtils {
	public static String getClientPath(String filePath) {
		String clientPath = null;

		// Finding the last occurrence of '\' and '.'
		int lastSlashIndex = filePath.lastIndexOf('\\');

		// Checking if '\' was found
		if (lastSlashIndex != -1) {
			// Retrieving the text after '\'
			String fileNameWithoutExtension = filePath.substring(lastSlashIndex + 1);

			// Replacing the .jpg extension with .txt
			clientPath = fileNameWithoutExtension.replace(".jpg", ".txt");
		}

		return clientPath;
	}

	public static void iterateOverTXTs(String dir) {
		Path dirPath = Paths.get(dir);
		if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.txt")) {
				for (Path entry : stream) {
					log.info("Processing file: {}", entry.toAbsolutePath());
				}
			} catch (IOException e) {
				log.error("Error accessing folder: {}", e.getMessage());
			}
		} else {
			log.error("The specified folder does not exist or is not a directory");
		}
	}

	private static void processFile(String filePath) {
		List<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.ISO_8859_1))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			log.error("Error reading file {}, {}", filePath, e.getMessage());
		}

		lines = cleanLines(lines);

		writeFile(filePath, lines);
	}

	private static List<String> cleanLines(List<String> lines) {
		List<String> filteredLines = new ArrayList<>();

		for (String line : lines) {
			if (!line.trim().isEmpty()) {
				filteredLines.add(line);
			}
		}

		// Remove the last line if the list is not empty
		if (!filteredLines.isEmpty()) {
			filteredLines.removeLast();
		}

		// Remove the first two lines if the list has at least two lines
		if (filteredLines.size() >= 2) {
			filteredLines = filteredLines.subList(2, filteredLines.size());
		} else {
			filteredLines.clear();  // If less than two lines, clear the list
		}

		return filteredLines;
	}

	private static void writeFile(String filePath, List<String> lines) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.ISO_8859_1))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			log.error("Error writing file {}, {}", filePath, e.getMessage());
		}
	}


	public static void main(String[] args) {
		iterateOverTXTs("src/main/resources/output2");
	}
}
