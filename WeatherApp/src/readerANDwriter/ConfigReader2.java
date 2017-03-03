package readerANDwriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import setupInterfaces.XMLelements;

public class ConfigReader2 implements XMLelements{
	private Document doc;
	private Element root, sources, sourcesbackup, details, values, storagelocation;
	private HashMap<String, String> elementValueMap;

	public HashMap<String, String> getElementValueMap() {
		return elementValueMap;
	}

	public ConfigReader2(String source) {
		elementValueMap = new HashMap<>();
		try {
			doc = new SAXBuilder().build(source);
			root = doc.getRootElement();
			sources = root.getChild(WEATHERSOURCES);
			sourcesbackup = root.getChild(WEATHERSOURCESBACKUP);
			details = root.getChild(WEATHERDETAILS);
			values = root.getChild(WEATHERVALUES);
			storagelocation = root.getChild(STORAGELOCATION);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}
	public void fill(){
		fillElementValueMap(sources);
		fillElementValueMap(sourcesbackup);
		fillElementValueMap(values);
		fillElementValueMap(storagelocation);
	}
	
	private void fillElementValueMap(Element element){
		 List<Element> tempChildrenList = element.getChildren();
	        for (int i = 0; i < tempChildrenList.size(); i++) {
	            Element child = tempChildrenList.get(i);
	            elementValueMap.put(child.getName(), child.getText());
	        }
	}

	public ArrayList<String> getCitysList() {
		List<Element> citys = details.getChildren(CITY);
		ArrayList<String> cityNames = new ArrayList<>();
		citys.forEach((city) -> cityNames.add(city.getText()));
		return cityNames;
	}

	public ArrayList<LocalDate> getDatesList() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		ArrayList<LocalDate> dates = new ArrayList<>();
		List<Element> elementDate = details.getChildren(DATE);
		for (int n = 0; n < elementDate.size(); n++) {
			String date = elementDate.get(n).getText();
			LocalDate selecteddate = LocalDate.parse(date, formatter);
			dates.add(n, selecteddate);
		}
		return dates;
	}	
}