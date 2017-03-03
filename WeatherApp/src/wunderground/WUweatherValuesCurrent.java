package wunderground;

public class WUweatherValuesCurrent implements WeatherValues, WUcurrentJSONelements {
	private RESTclient rct;

	public WUweatherValuesCurrent(String key, String city) {
		rct = new RESTclient(key, "current", city, 0);
	}

	@Override
	public boolean isValid() {
		return rct.isValid();
	}

	@Override
	public String getCity() {
		return rct.getWeatherDataMap().get(CITY).toString();
	}

	@Override
	public String getDateOfWeather() {
		return rct.getWeatherDataMap().get(DATE).toString();
	}

	@Override
	public String getDescription() {
		return rct.getWeatherDataMap().get(DESCRIPTION).toString();
	}

	@Override
	public String getHumidity() {
		return rct.getWeatherDataMap().get(HUMIDITY).toString();
	}

	@Override
	public String getTemperatureInC() {
		return rct.getWeatherDataMap().get(TEMP_IN_C).toString();
	}

	@Override
	public String getTemperatureInF() {
		return rct.getWeatherDataMap().get(TEMP_IN_F).toString();
	}

	@Override
	public String getWindSpeedInKPH() {
		return rct.getWeatherDataMap().get(WIND_IN_KPH).toString();
	}

	@Override
	public String getWindSpeedInMPH() {
		return rct.getWeatherDataMap().get(WIND_IN_MPH).toString();
	}

	@Override
	public String getWindDegrees() {
		return rct.getWeatherDataMap().get(WIND_DEGREES).toString();
	}

	@Override
	public String getWindDirection() {
		return rct.getWeatherDataMap().get(WIND_DIRECTION).toString();
	}

	public String getWindDescription() {
		return rct.getWeatherDataMap().get(WIND_DESCRIPTION).toString();
	}

	@Override
	public String getPressureInHPA() {
		return rct.getWeatherDataMap().get(PRESSURE_IN_HPA).toString();
	}
}