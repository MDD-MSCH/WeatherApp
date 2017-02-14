package wunderground;

import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

import readerANDwriter.LogfileWriter;

public class WUweatherValuesForecast implements WeatherValues, WUforecastJSONelements {
	private LogfileWriter logWriter;
	private RESTclient rct;

	public WUweatherValuesForecast(String key, String city, int daysInFuture) {
		logWriter = new LogfileWriter();
		rct = new RESTclient(key, "forecast", city, daysInFuture);
	}

	@Override
	public boolean isValid() {
		return rct.isValid();
	}

	@Override
	public String getCity() {
		return getValuesAbout(DATE, "tz_long");
	}

	@Override
	public String getDateOfWeather() {
		return getValuesAbout(DATE, "pretty");
	}

	@Override
	public String getDescription() {
		return rct.getWeatherDataMap().get(DESCRIPTION_DAY_METRIC).toString();
	}

	@Override
	public String getHumidity() {
		return rct.getWeatherDataMap().get(HUMIDITY).toString();
	}

	@Override
	public String getTemperatureInC() {
		return getValuesAbout(TEMPERATURE_LOW, "celsius");
	}

	@Override
	public String getTemperatureInF() {
		return getValuesAbout(TEMPERATURE_LOW, "fahrenheit");
	}

	@Override
	public String getWindSpeedInKPH() {
		return getValuesAbout(WIND, "kph");
	}

	@Override
	public String getWindSpeedInMPH() {
		return getValuesAbout(WIND, "mph");
	}

	@Override
	public String getWindDegrees() {
		return getValuesAbout(WIND, "degrees");
	}

	@Override
	public String getWindDirection() {
		return getValuesAbout(WIND, "dir");
	}

	@Override
	public String getPressureInHPA() {
		return "Only avoidable in current weather";
	}

	private String getValuesAbout(String key, String value) {
		JSONObject jsonObject = (JSONObject) rct.getWeatherDataMap().get(key);
		String name = "no value avoidable";
		try {
			name = jsonObject.get(value).toString();
			System.out.println(name + " name");
		} catch (JSONException e) {
			logWriter.appendLine(e.toString() + " " + Arrays.toString(e.getStackTrace()));
		}
		return name;
	}
}