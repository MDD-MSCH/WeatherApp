package readerANDwriter;

import org.xml.sax.*;
import setupInterfaces.XMLelements;
import org.w3c.dom.*;
import java.util.Arrays;
import java.util.HashMap;
import javax.xml.parsers.*;

public class ConfigReader implements XMLelements{
	private LogfileWriter logWriter = new LogfileWriter();
	private HashMap<String, String> elementValueMap = new HashMap<>();
	
	
	public HashMap<String, String> getElementValueMap() {
		return elementValueMap;
	}

	public ConfigReader(String source) {
		Document xmlDoc = getDocument(source);

		NodeList listOfWeathersources = xmlDoc.getElementsByTagName("weathersources");
		NodeList listOfWeathersourcesBackup = xmlDoc.getElementsByTagName("weathersourcesbackup");
		NodeList listOfWeatherdetails = xmlDoc.getElementsByTagName("weatherdetails");
		NodeList listOfWeathervalues = xmlDoc.getElementsByTagName("weathervalues");
		NodeList listOfStoragelocation = xmlDoc.getElementsByTagName("storagelocation");
	
		String[] elementNamesWeathersources = { URL, APIKEY, UPDATEFREQUENCY, REPEAT };
		String[] elementNamesWeathersourcesBackup = { URLBACKUP, APIKEYBACKUP, UPDATEFREQUENCYBACKUP, REPEATBACKUP };
		String[] elementNamesWeatherdetails = { CITY, DATE };
		String[] elementNamesWeathervalues = { WEATHERDESCRIPTION, TEMPERATURE, HUMIDITY, PRESSURE, WINDSPEED, WINDDIRECTION, SUNRISE, SUNSET };
		String[] elementNamesStoragelocation = { PATH, FILENAME, FILEFORMAT };

		String attrName = "unit";
		getElementsAndValues(listOfWeathersources, elementNamesWeathersources, attrName);
		getElementsAndValues(listOfWeathersourcesBackup, elementNamesWeathersourcesBackup, attrName);
		getElementsAndValues(listOfWeatherdetails, elementNamesWeatherdetails, attrName);
		getElementsAndValues(listOfWeathervalues, elementNamesWeathervalues, attrName);
		getElementsAndValues(listOfStoragelocation, elementNamesStoragelocation, attrName);
	}

	private Document getDocument(String source) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(source));
		} catch (Exception e) {
			logWriter.appendLine(e.toString()+" "+Arrays.toString(e.getStackTrace()));
		}
		return null;
	}

	private void getElementsAndValues(NodeList listOf, String[] elementName, String attrName) {
		try {
			for (int i = 0; i < listOf.getLength(); i++) {
				Node weatherappNode = listOf.item(i);
				Element weatherElement = (Element) weatherappNode;
				for (int j = 0; j < elementName.length; j++) {
					NodeList weatherAppList = weatherElement.getElementsByTagName(elementName[j]);
					Element weatherAppElement = (Element) weatherAppList.item(0);
					NodeList elementList = weatherAppElement.getChildNodes();

					if (weatherAppElement.hasAttribute(attrName)) {
						elementValueMap.put(elementName[j], elementList.item(0).getNodeValue().trim().toString());
					} else {
						elementValueMap.put(elementName[j], elementList.item(0).getNodeValue().trim().toString());
					}
				}
			}
		} catch (Exception e) {
			logWriter.appendLine(e.toString()+" "+Arrays.toString(e.getStackTrace()));
		}
	}
}