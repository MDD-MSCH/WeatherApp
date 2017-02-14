package wunderground;

public interface WeatherValues {
	boolean isValid();
	String getCity();
	String getDateOfWeather();
	String getDescription();
	String getTemperatureInC();
	String getTemperatureInF();
	String getHumidity();
	String getPressureInHPA();
	String getWindSpeedInKPH();
	String getWindSpeedInMPH();
	String getWindDirection();
	String getWindDegrees();
}