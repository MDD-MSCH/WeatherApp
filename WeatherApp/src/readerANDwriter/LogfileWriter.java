package readerANDwriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import setupInterfaces.SystemProps;


public enum LogfileWriter implements SystemProps {
	INSTANCE_LOG_WRITER;
	
	public void appendLine(String message){
		LocalDateTime currentDateTime = LocalDateTime.now();
		String time = currentDateTime.toString();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FULL_PATH_TO_LOGFILE, true))) 
		{
		    writer.write(time+": "+message+LINE_SEPARATOR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}