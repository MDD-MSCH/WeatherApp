package weatherapp;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import readerANDwriter.CSVwriter;
import readerANDwriter.ConfigReader2;
import readerANDwriter.LogfileWriter;
import setupInterfaces.SystemProps;

public class WeatherApp implements SystemProps {
	private int count = 0;
	private String path;

	public WeatherApp() {
		path = "./src/config.xml";
		LogfileWriter logwriter = new LogfileWriter();

		if (checkPath()) {
			path = FULL_PATH_TO_XML_CONFIG;
		} else {
			logwriter.appendLine("No file found in: " + FULL_PATH_TO_XML_CONFIG+ " ,take the default config.xml which you can find in: " + path);
		}
		ConfigReader2 xmlConfigreader2 = new ConfigReader2(path);
		xmlConfigreader2.fill();
		int updatefrequency = Integer.parseInt(xmlConfigreader2.getElementValueMap().get("updatefrequency"));
		int repeat = Integer.parseInt(xmlConfigreader2.getElementValueMap().get("repeat"));
		Timer caretaker = new Timer();
		TimerTask action = new TimerTask() {
			public void run() {
				for (int n = 0; n < xmlConfigreader2.getCitysList().size(); n++) {
					Preparation prepare = new Preparation(xmlConfigreader2.getElementValueMap(), xmlConfigreader2.getCitysList().get(n), xmlConfigreader2.getDatesList().get(n));
					new CSVwriter(prepare.getWeatherDataMap(), prepare.getPath(), prepare.getFilename(), prepare.getFileformat());
				}
				count++;
				if (count > repeat) {
					caretaker.cancel();
					caretaker.purge();
					return;
				}
			}
		};
		caretaker.schedule(action, 500, 60000 * updatefrequency);
	}

	public static void main(String[] args) {
		new WeatherApp();
	}

	private boolean checkPath() {
		File file = new File(FULL_PATH_TO_XML_CONFIG);
		if (file.exists()) {
			return true;
		}
		return false;
	}
}