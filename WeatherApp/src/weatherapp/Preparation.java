package weatherapp;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import owm.CurrentWeather;
import owm.DailyForecast;
import owm.OpenWeatherMap;
import owm.Tools;
import readerANDwriter.LogfileWriter;
import setupInterfaces.XMLelements;
import wunderground.WUweatherValuesCurrent;
import wunderground.WUweatherValuesForecast;

public class Preparation implements XMLelements {
	private OpenWeatherMap owm;
	private CurrentWeather owmWeatherToday;
	private DailyForecast owmWeatherInFuture;
	private Tools convert;

	private WUweatherValuesCurrent wuWeatherToday;
	private WUweatherValuesForecast wuWeatherInFuture;
	private ConfigValues cv;
	private LogfileWriter logWriter;

	private TreeMap<String, String> weatherDataMap;
	private LocalDate date, currentDate, maxForcastDate;
	private String path;
	private String filename;
	private String fileformat;
	private String cityName;
	private boolean current;
	private int daysInFuture;

	public Preparation(HashMap<String, String> elementValueMap, String cityName, LocalDate date) {
		this.cityName = cityName;
		this.date = date;
		init(elementValueMap);
		checkCurrentOrForecast();
	}
	
	public TreeMap<String, String> getWeatherDataMap(){
		return weatherDataMap;
	}

	public String getPath() {
		return path;
	}

	public String getFilename() {
		return filename;
	}

	public String getFileformat() {
		return fileformat;
	}

	private void init(HashMap<String, String> elementValueMap) {
		logWriter = new LogfileWriter();
		cv = new ConfigValues(elementValueMap);
		owm = new OpenWeatherMap(cv.getApiKey());
		convert = new Tools();
		weatherDataMap = new TreeMap<>();
		
		currentDate = LocalDate.now();
		maxForcastDate = LocalDate.now().plusDays(4);
		path = cv.getPath();
		filename = cv.getFilename();
		fileformat = cv.getFileformat();
	}

	private void checkCurrentOrForecast() {
		if (date.equals(currentDate)) {
			current = true;
			checkReachabilityOfCurrentWeatherServices();
		}

		if (date.isAfter(currentDate) && date.isBefore(maxForcastDate)) {
			Period period = Period.between(currentDate, date);
			daysInFuture = period.getDays();
			checkReachabilityOfForecastWeatherServices();			
		} 
		
		if (date.isBefore(currentDate) || date.isAfter(maxForcastDate)) {
			logWriter.appendLine("Selected date is not avoidable!");
			System.exit(0);
		}
	}
	
	private void checkReachabilityOfCurrentWeatherServices(){
		if (checkReachabilityOfOWMcurrent()) {
			weatherDataMap.put(CITY, owmWeatherToday.getCityName());
			weatherDataMap.put(DATE, date.toString());
			fillListWithOWMweather();
		} else if (checkReachabilityOfWundergroundCurrent()) {
			weatherDataMap.put(CITY, wuWeatherToday.getCity());
			weatherDataMap.put(DATE, date.toString());
			fillListWithWUweather();
		} else {
			logWriter.appendLine("No current weatherservice is reachable!");
		}
	}
	
	private void checkReachabilityOfForecastWeatherServices(){
		if (checkReachabilityOfOWMforcast()) {
			weatherDataMap.put(CITY, owmWeatherInFuture.getCityInstance().getCityName());
			weatherDataMap.put(DATE, date.toString());
			fillListWithOWMweather();
		} else if (checkReachabilityOfWundergroundForecast()) {
			weatherDataMap.put(CITY, wuWeatherInFuture.getCity());
			weatherDataMap.put(DATE, date.toString());
			fillListWithWUweather();
		} else {
			logWriter.appendLine("No forecast weatherservice is reachable!");
		}
	}

	private boolean checkReachabilityOfOWMcurrent() {
		try {
			owmWeatherToday = owm.currentWeatherByCityName(cityName);
		} catch (Exception e) {
			logWriter.appendLine(e.toString() + " " + Arrays.toString(e.getStackTrace()));
		}
		return owmWeatherToday.isValid();
	}

	private boolean checkReachabilityOfWundergroundCurrent() {
		wuWeatherToday = new WUweatherValuesCurrent(cv.getApiKeyBackup(), cityName);
		return wuWeatherToday.isValid();
	}

	private boolean checkReachabilityOfOWMforcast() {
		byte i = 4;
		try {
			owmWeatherInFuture = owm.dailyForecastByCityName(cityName, i);
		} catch (Exception e) {
			logWriter.appendLine(e.toString() + " " + Arrays.toString(e.getStackTrace()));
		}
		return owmWeatherInFuture.isValid();
	}

	private boolean checkReachabilityOfWundergroundForecast() {
		wuWeatherInFuture = new WUweatherValuesForecast(cv.getApiKeyBackup(), cityName, daysInFuture);
		return wuWeatherInFuture.isValid();
	}

	private void fillListWithOWMweather() {
		addOWMweatherDescription();
		addOWMweatherTemperature();
		addOWMweatherHumidity();
		addOWMweatherPressure();
		addOWMweatherWindspeed();
		addOWMweatherWindDirection();
		addOWMweatherSunrise();
		addOWMweatherSunset();
	}

	private void addOWMweatherDescription() {
		String weatherData = "";
		if (cv.isWeather() && current) {
			if (owmWeatherToday.hasCloudsInstance() && owmWeatherToday.getCloudsInstance().hasPercentageOfClouds()) {
				weatherData = " Clouds: " + owmWeatherToday.getCloudsInstance().getPercentageOfClouds() + "%";
			}
			if (owmWeatherToday.hasRainInstance() && owmWeatherToday.getRainInstance().hasRain3h()) {
				weatherData = " Rain: " + owmWeatherToday.getRainInstance().getRain3h();
			}
			if (owmWeatherToday.hasSnowInstance() && owmWeatherToday.getSnowInstance().hasSnow3h()) {
				weatherData = " Snow: " + owmWeatherToday.getSnowInstance().getSnow3h();
			}
			weatherDataMap.put(WEATHERDESCRIPTION,owmWeatherToday.getWeatherInstance(0).getWeatherDescription() + weatherData + " Map");
		} else if (cv.isWeather() && !current) {
			if (owmWeatherInFuture.getForecastInstance(daysInFuture).hasPercentageOfClouds()) {
				weatherData = " Clouds: " + owmWeatherInFuture.getForecastInstance(daysInFuture).getPercentageOfClouds()+ "%";
			}
			if (owmWeatherInFuture.getForecastInstance(daysInFuture).hasRain()) {
				weatherData = " Rain: " + owmWeatherInFuture.getForecastInstance(daysInFuture).getRain();
			}
			if (owmWeatherInFuture.getForecastInstance(daysInFuture).hasSnow()) {
				weatherData = " Snow: " + owmWeatherInFuture.getForecastInstance(daysInFuture).getSnow();
			}
			weatherDataMap.put(WEATHERDESCRIPTION, owmWeatherInFuture.getForecastInstance(daysInFuture).getWeatherInstance(0).getWeatherDescription()+ weatherData);
		} else {
			weatherDataMap.put(WEATHERDESCRIPTION, "not selected");
		}
	}
	
	private void addOWMweatherTemperature() {
		if (cv.isTemperature() && current) {
			float tempGradCelsius = convert.convertFahrenheit2Celsius(owmWeatherToday.getMainInstance().getTemperature());
			float tempFahrenheit = owmWeatherToday.getMainInstance().getTemperature();
			weatherDataMap.put(TEMPERATURE, tempGradCelsius + "°C/" + tempFahrenheit + "°F");
		} else if (cv.isTemperature() && !current) {
			float tempGradCelsius = convert.convertFahrenheit2Celsius(owmWeatherInFuture.getForecastInstance(daysInFuture).getTemperatureInstance().getMaximumTemperature());
			float tempFahrenheit = owmWeatherInFuture.getForecastInstance(daysInFuture).getTemperatureInstance().getMaximumTemperature();
			weatherDataMap.put(TEMPERATURE,tempGradCelsius + "°C/" + tempFahrenheit + "°F");
		} else {
				weatherDataMap.put(TEMPERATURE,"not selected");
		}
	}
	
	private void addOWMweatherHumidity() {
		if (cv.isHumidity() && current) {
			weatherDataMap.put(HUMIDITY, owmWeatherToday.getMainInstance().getHumidity() + "%");
		} else if (cv.isHumidity() && !current) {
			weatherDataMap.put(HUMIDITY,owmWeatherInFuture.getForecastInstance(daysInFuture).getHumidity() + "%");
		} else {
			weatherDataMap.put(HUMIDITY,"not selected");
		}
	}
	
	private void addOWMweatherPressure() {
		if (cv.isPressure() && current) {
			weatherDataMap.put(PRESSURE, owmWeatherToday.getMainInstance().getPressure() + "hPa");
		} else if (cv.isPressure() && !current) {
			weatherDataMap.put(PRESSURE,owmWeatherInFuture.getForecastInstance(daysInFuture).getPressure() + "hPa");
		} else {
			weatherDataMap.put(PRESSURE,"not selected");
		}
	}
	
	private void addOWMweatherWindspeed() {
		if (cv.isWindspeed() && current) {
			weatherDataMap.put(WINDSPEED, owmWeatherToday.getWindInstance().getWindSpeed() + "m/s");
		} else if (cv.isWindspeed() && !current) {
			weatherDataMap.put(WINDSPEED,owmWeatherInFuture.getForecastInstance(daysInFuture).getWindSpeed() + "m/s");
		} else {
			weatherDataMap.put(WINDSPEED,"not selected");
		}
	}
	
	private void addOWMweatherWindDirection() {
		if (cv.isWinddirection() && current) {
			weatherDataMap.put(WINDDIRECTION, convert.convertDegree2Direction(owmWeatherToday.getWindInstance().getWindDegree()));
		} else if (cv.isWinddirection() && !current) {
			weatherDataMap.put(WINDDIRECTION,convert.convertDegree2Direction(owmWeatherInFuture.getForecastInstance(daysInFuture).getWindDegree()));
		} else {
			weatherDataMap.put(WINDDIRECTION,"not selected");
		}
	}
	
	private void addOWMweatherSunrise() {
		if (cv.isSunrise() && current) {
			weatherDataMap.put(SUNRISE, owmWeatherToday.getSysInstance().getSunriseTime().toString());
		} else if (cv.isSunrise() && !current) {
			weatherDataMap.put(SUNRISE,"not avoidable");
		} else {
			weatherDataMap.put(SUNRISE,"not selected");
		}
	}
	
	private void addOWMweatherSunset(){
		if (cv.isSunset() && current) {
			weatherDataMap.put(SUNSET, owmWeatherToday.getSysInstance().getSunsetTime().toString());
		} else if (cv.isSunset() && !current) {
			weatherDataMap.put(SUNSET,"not avoidable");
		} else {
			weatherDataMap.put(SUNSET,"not selected8");
		}
	}
	
	private void fillListWithWUweather() {
		addWUweatherDescription();
		addWUweatherTemperature();
		addWUweatherHumidity();
		addWUweatherPressure();
		addWUweatherWindspeed();
		addWUweatherWindDirection();
		addWUweatherSunrise();
		addWUweatherSunset();
	}

	private void addWUweatherDescription() {
		if (cv.isWeather() && current) {
			weatherDataMap.put(WEATHERDESCRIPTION, wuWeatherToday.getDescription() + " " + wuWeatherToday.getWindDescription());
		} else if (cv.isWeather() && !current) {
			weatherDataMap.put(WEATHERDESCRIPTION,wuWeatherInFuture.getDescription());
		} else {
			weatherDataMap.put(WEATHERDESCRIPTION,"not selected");
		}
	}

	private void addWUweatherTemperature() {
		if (cv.isTemperature() && current) {
			weatherDataMap.put(TEMPERATURE, wuWeatherToday.getTemperatureInC() + "°C/" + wuWeatherToday.getTemperatureInF() + "°F");
		} else if (cv.isTemperature() && !current) {
			weatherDataMap.put(TEMPERATURE, wuWeatherInFuture.getTemperatureInC() + "°C");
		} else {
			weatherDataMap.put(TEMPERATURE, "not selected");
		}
	}

	private void addWUweatherHumidity() {
		if (cv.isHumidity() && current) {
			weatherDataMap.put(HUMIDITY, wuWeatherToday.getHumidity());
		} else if (cv.isHumidity() && !current) {
			weatherDataMap.put(HUMIDITY,wuWeatherInFuture.getHumidity() + "%");
		} else {
			weatherDataMap.put(HUMIDITY,"not selected");
		}
	}

	private void addWUweatherPressure() {
		if (cv.isPressure() && current) {
			weatherDataMap.put(PRESSURE, wuWeatherToday.getPressureInHPA() + "hPa");
		} else if (cv.isPressure() && !current) {
			weatherDataMap.put(PRESSURE, wuWeatherInFuture.getPressureInHPA());
		} else {
			weatherDataMap.put(PRESSURE,"not selected");
		}
	}

	private void addWUweatherWindspeed() {
		if (cv.isWindspeed() && current) {
			weatherDataMap.put(WINDSPEED, wuWeatherToday.getWindSpeedInKPH() + "kph");
		} else if (cv.isWindspeed() && !current) {
			weatherDataMap.put(WINDSPEED, wuWeatherInFuture.getWindSpeedInKPH() + "kph");
		} else {
			weatherDataMap.put(WINDSPEED, "not selected");
		}
	}

	private void addWUweatherWindDirection() {
		if (cv.isWinddirection() && current) {
			weatherDataMap.put(WINDDIRECTION, wuWeatherToday.getWindDirection());
		} else if (cv.isWinddirection() && !current) {
			weatherDataMap.put(WINDDIRECTION, wuWeatherInFuture.getWindDirection());
		} else {
			weatherDataMap.put(WINDDIRECTION, "not selected");
		}
	}

	private void addWUweatherSunrise() {
		if (cv.isSunrise() && current) {
			weatherDataMap.put(SUNRISE, "not avoidable");
		} else if (cv.isSunrise() && !current) {
			weatherDataMap.put(SUNRISE, "not avoidable");
		} else {
			weatherDataMap.put(SUNRISE, "not selected");
		}
	}

	private void addWUweatherSunset() {
		if (cv.isSunset() && current) {
			weatherDataMap.put(SUNSET, "not avoidable");
		} else if (cv.isSunset() && !current) {
			weatherDataMap.put(SUNSET, "not avoidable");
		} else {
			weatherDataMap.put(SUNSET, "not selected");
		}
	}
}