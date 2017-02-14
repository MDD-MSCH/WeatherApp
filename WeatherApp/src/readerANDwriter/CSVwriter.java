package readerANDwriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.TreeMap;

import setupInterfaces.SystemProps;
import setupInterfaces.XMLelements;

public class CSVwriter implements XMLelements, SystemProps {

	private LogfileWriter logWriter;
	private TreeMap<String, Integer> fileHeaderWordPositions;
	private TreeMap<String, String> weatherData;
	private String[] weatherDataReadyToWrite;
	
	private static final String PATH_BACKUP = PATH_TO_HOME + FILE_SEPARATOR + "test.csv";
	private static final String DELIMITER = PATH_SEPARATOR;
	private static final String NEW_LINE_SEPARATOR = LINE_SEPARATOR;
	private static final String FILE_HEADER = CITY + DELIMITER + DATE + DELIMITER + WEATHERDESCRIPTION + DELIMITER
			+ TEMPERATURE + DELIMITER + HUMIDITY + DELIMITER + PRESSURE + DELIMITER + WINDSPEED + DELIMITER
			+ WINDDIRECTION + DELIMITER + SUNRISE + DELIMITER + SUNSET + DELIMITER;
	
	private String path;
	private String filename;
	private String fileformat;
	
	public CSVwriter(TreeMap<String, String> weatherDataMap, String path, String filename, String fileformat) {
		logWriter = new LogfileWriter();
		fileHeaderWordPositions = new TreeMap<>();
		weatherData = weatherDataMap;
		this.path = path;
		this.filename = filename;
		this.fileformat = fileformat;
		checkWordPositions(FILE_HEADER);
		weatherDataReadyToWrite = new String [fileHeaderWordPositions.size()];
		fillWeatherDataReadyToWrite();
		writeCsvFile();
	}
	
	private void fillWeatherDataReadyToWrite(){
		weatherDataReadyToWrite[fileHeaderWordPositions.get(CITY)] = weatherData.get(CITY);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(DATE)] = weatherData.get(DATE);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(WEATHERDESCRIPTION)] = weatherData.get(WEATHERDESCRIPTION);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(TEMPERATURE)] = weatherData.get(TEMPERATURE);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(HUMIDITY)] = weatherData.get(HUMIDITY);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(PRESSURE)] = weatherData.get(PRESSURE);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(WINDSPEED)] = weatherData.get(WINDSPEED);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(WINDDIRECTION)] = weatherData.get(WINDDIRECTION);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(SUNRISE)] = weatherData.get(SUNRISE);
		weatherDataReadyToWrite[fileHeaderWordPositions.get(SUNSET)] = weatherData.get(SUNSET);
	}

	private void writeCsvFile() {
		File file = new File(path + filename + fileformat);
		boolean temp = (file.exists());
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true), "UTF-8"))) {
			if (!temp) {
				out.append(FILE_HEADER.toString());
				out.append(NEW_LINE_SEPARATOR);
			}
			for (String weatherData : weatherDataReadyToWrite) {
				out.append(weatherData);
				out.append(DELIMITER);
			}
			out.append(NEW_LINE_SEPARATOR);
			out.flush();
			logWriter.appendLine("CSV file was written successfully !!!");
		} catch (IOException e) {
			logWriter.appendLine("Error in CSVwriter !!!" + e.toString() + " " + Arrays.toString(e.getStackTrace()));
		}
	}
	
	private void checkWordPositions(String header) {
		int wordCount = 0;
		int endOfLine = header.length() - 1;
		boolean word = false;
		char[] nameOfWord = new char[50];

		for (int i = 0, array = 0; i < header.length(); i++, array++) {
			if (Character.isLetter(header.charAt(i)) && i != endOfLine) {
				word = true;
				nameOfWord[array] = header.charAt(i);
			} else if (!Character.isLetter(header.charAt(i)) && word) {
				String spellIt = new String(nameOfWord);
				fileHeaderWordPositions.put(spellIt.toLowerCase().trim(), wordCount);
				nameOfWord = new char[20];
				array = 0;
				word = false;
				wordCount++;
			} else if (Character.isLetter(header.charAt(i)) && i == endOfLine) {
				String spellIt = new String(nameOfWord);
				fileHeaderWordPositions.put(spellIt.toLowerCase().trim(), wordCount);
				wordCount++;
			}
		}
	}
}