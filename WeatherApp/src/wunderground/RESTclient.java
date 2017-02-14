package wunderground;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import readerANDwriter.LogfileWriter;

public class RESTclient {
	private static final String URL = "http://api.wunderground.com/api/";

	private LogfileWriter logWriter;
	private WebTarget wetterApiData;

	private JSONObject weatherData;
	private JSONObject error;
	private JSONObject structure;
	private JSONObject inTxtForecast;
	private JSONObject inSimpleForcast;

	private HashMap<String, Object> weatherDataMap;
	private boolean valid;

	public HashMap<String, Object> getWeatherDataMap() {
		return weatherDataMap;
	}

	public boolean isValid() {
		return valid;
	}

	public RESTclient(String apikey, String date, String city, int daysInFuture) {
		Client wetterApiClient = ClientBuilder.newClient();
		logWriter = new LogfileWriter();
		weatherDataMap = new HashMap<>();
		try {
			if (date.equals("current")) {
				wetterApiData = wetterApiClient.target(URL + apikey + "/conditions/lang:EN/q/Germany/" + city + ".json");
				weatherData = new JSONObject(wetterApiData.request(MediaType.APPLICATION_JSON).get(String.class));
				error = (JSONObject) weatherData.get("response");
				if (checkReachability()) {
					structure = (JSONObject) weatherData.get("current_observation");
					putCurrentWeatherInMap();
				}
			}
			if (date.equals("forecast")) {
				wetterApiData = wetterApiClient.target(URL + apikey + "/forecast/lang:EN/q/Germany/" + city + ".json");
				weatherData = new JSONObject(wetterApiData.request(MediaType.APPLICATION_JSON).get(String.class));
				error = (JSONObject) weatherData.get("response");
				if (checkReachability()) {
					structure = (JSONObject) weatherData.get("forecast");
					inTxtForecast = (JSONObject) structure.get("txt_forecast");
					inSimpleForcast = (JSONObject) structure.get("simpleforecast");
					JSONArray jArray = (JSONArray) inTxtForecast.get("forecastday");
					switch (daysInFuture) {
					case 1:
						putForcastWeatherInMap(jArray, " text ", 0, (daysInFuture - 1));
						putForcastWeatherInMap(jArray, " text ", 1, daysInFuture);
						break;
					case 2:
						putForcastWeatherInMap(jArray, " text ", 0, daysInFuture);
						putForcastWeatherInMap(jArray, " text ", 1, (daysInFuture + 1));
						break;
					case 3:
						putForcastWeatherInMap(jArray, " text ", 0, (daysInFuture + 1));
						putForcastWeatherInMap(jArray, " text ", 1, (daysInFuture + 2));
						break;
					case 4:
						putForcastWeatherInMap(jArray, " text ", 0, (daysInFuture + 2));
						putForcastWeatherInMap(jArray, " text ", 1, (daysInFuture + 3));
						break;
					}
					jArray = (JSONArray) inSimpleForcast.get("forecastday");
					putForcastWeatherInMap(jArray, " simple ", 0, daysInFuture - 1);
				}
			}
		} catch (JSONException e) {
			logWriter.appendLine(e.toString() + " " + Arrays.toString(e.getStackTrace()));
		}
	}

	private boolean checkReachability() {
		valid = false;
		Iterator<?> keys = error.keys();
		try {
			while (keys.hasNext()) {
				String key = (String) keys.next();
				Object value = error.get(key);
				weatherDataMap.put(key, value);
			}
			if (weatherDataMap.containsKey("error")) {
				logWriter.appendLine(weatherDataMap.get("error").toString());
			}
		} catch (JSONException e) {
			logWriter.appendLine(e.toString() + " " + Arrays.toString(e.getStackTrace()));
		}
		return valid = true;
	}

	private void putCurrentWeatherInMap() {
		Iterator<?> keys = structure.keys();
		try {
			while (keys.hasNext()) {
				String key = (String) keys.next();
				Object value = structure.get(key);
				weatherDataMap.put(key, value);
			}
		} catch (JSONException e) {
			logWriter.appendLine(e.toString() + " " + Arrays.toString(e.getStackTrace()));
		}
	}

	private void putForcastWeatherInMap(JSONArray jArray, String forecast, int period, int day) {
		try {
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject inJarray = jArray.getJSONObject(day);
				Iterator<?> keys = inJarray.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					Object value = inJarray.get(key);
					weatherDataMap.put(key + forecast + period, value);
				}
			}
			weatherDataMap.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
		} catch (JSONException e1) {
			logWriter.appendLine(e1.toString() + " " + Arrays.toString(e1.getStackTrace()));
		}
	}
}