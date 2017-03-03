package weatherapp;

import java.util.Arrays;
import java.util.HashMap;
import readerANDwriter.LogfileWriter;
import setupInterfaces.XMLelements;

public class ConfigValues implements XMLelements {
	private LogfileWriter logWriter;
	private String url, urlBackup, apiKey, apiKeyBackup, path, filename, fileformat;
	private int updatefrequency, updatefrequencyBackup, repeat, repeatBackup;
	private boolean weather, temperature, humidity, pressure, windspeed, winddirection, sunrise, sunset;
	
	public boolean isWeather() {
		return this.weather;
	}

	public void setWeather(boolean weather) {
		this.weather = weather;
	}

	public boolean isTemperature() {
		return this.temperature;
	}

	public void setTemperature(boolean temperature) {
		this.temperature = temperature;
	}

	public boolean isHumidity() {
		return this.humidity;
	}

	public void setHumidity(boolean humidity) {
		this.humidity = humidity;
	}

	public boolean isPressure() {
		return this.pressure;
	}

	public void setPressure(boolean pressure) {
		this.pressure = pressure;
	}

	public boolean isWindspeed() {
		return this.windspeed;
	}

	public void setWindspeed(boolean windspeed) {
		this.windspeed = windspeed;
	}

	public boolean isWinddirection() {
		return this.winddirection;
	}

	public void setWinddirection(boolean winddirection) {
		this.winddirection = winddirection;
	}

	public boolean isSunrise() {
		return this.sunrise;
	}

	public void setSunrise(boolean sunrise) {
		this.sunrise = sunrise;
	}

	public boolean isSunset() {
		return this.sunset;
	}

	public void setSunset(boolean sunset) {
		this.sunset = sunset;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlBackup() {
		return this.urlBackup;
	}

	public void setUrlBackup(String urlBackup) {
		this.urlBackup = urlBackup;
	}

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiKeyBackup() {
		return this.apiKeyBackup;
	}

	public void setApiKeyBackup(String apiKeyBackup) {
		this.apiKeyBackup = apiKeyBackup;
	}

	public int getUpdatefrequency() {
		return this.updatefrequency;
	}

	public void setUpdatefrequency(int updatefrequency) {
		this.updatefrequency = updatefrequency;
	}

	public int getUpdatefrequencyBackup() {
		return this.updatefrequencyBackup;
	}

	public void setUpdatefrequencyBackup(int updatefrequencyBackup) {
		this.updatefrequencyBackup = updatefrequencyBackup;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public int getRepeatBackup() {
		return repeatBackup;
	}

	public void setRepeatBackup(int repeatBackup) {
		this.repeatBackup = repeatBackup;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileformat() {
		return this.fileformat;
	}

	public void setFileformat(String fileformat) {
		this.fileformat = fileformat;
	}
	public ConfigValues(HashMap<String, String> elementValueMap) {
		logWriter = LogfileWriter.INSTANCE_LOG_WRITER;
		try {
//			elementValueMap.forEach((k,v)->System.out.println("KeyCV : " + k + " ValueCV : " + v));
			url = elementValueMap.get(URL);
			urlBackup = elementValueMap.get(URLBACKUP);
			apiKey = elementValueMap.get(APIKEY);
			apiKeyBackup = elementValueMap.get(APIKEYBACKUP);
			updatefrequency = Integer.parseInt(elementValueMap.get(UPDATEFREQUENCY));
			updatefrequencyBackup = Integer.parseInt(elementValueMap.get(UPDATEFREQUENCYBACKUP));
			repeat = Integer.parseInt(elementValueMap.get(REPEAT));
			repeatBackup = Integer.parseInt(elementValueMap.get(REPEATBACKUP));
			weather = Boolean.parseBoolean(elementValueMap.get(WEATHERDESCRIPTION));
			temperature = Boolean.parseBoolean(elementValueMap.get(TEMPERATURE));
			humidity = Boolean.parseBoolean(elementValueMap.get(HUMIDITY));
			pressure = Boolean.parseBoolean(elementValueMap.get(PRESSURE));
			windspeed = Boolean.parseBoolean(elementValueMap.get(WINDSPEED));
			winddirection = Boolean.parseBoolean(elementValueMap.get(WINDDIRECTION));
			sunrise = Boolean.parseBoolean(elementValueMap.get(SUNRISE));
			sunset = Boolean.parseBoolean(elementValueMap.get(SUNSET));
			path = elementValueMap.get(PATH);
			filename = elementValueMap.get(FILENAME);
			fileformat = elementValueMap.get(FILEFORMAT);
		} catch (Exception e) {
			logWriter.appendLine(e.toString()+" "+Arrays.toString(e.getStackTrace()));
		} 
	}
}