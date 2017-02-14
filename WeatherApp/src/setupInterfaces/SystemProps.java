package setupInterfaces;

public interface SystemProps {
	String PATH_TO_HOME = System.getProperty("user.home");
	String FILE_SEPARATOR = System.getProperty("file.separator");
	String PATH_SEPARATOR = System.getProperty("path.separator");
	String LINE_SEPARATOR = System.getProperty("line.separator");
	String CONFIG_NAME = "config.xml";
	String FULL_PATH_TO_XML_CONFIG = PATH_TO_HOME+FILE_SEPARATOR+CONFIG_NAME;
	String LOG_FILENAME = "WeatherAppLogfile.txt";
	String FULL_PATH_TO_LOGFILE = PATH_TO_HOME + FILE_SEPARATOR + LOG_FILENAME;
}