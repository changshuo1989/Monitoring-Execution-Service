package com.execution.service.monitoring_execution_service.utility;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyReader {
	private static String propertyPath="/application.properties";
	private static String jsonPropertyPath = "json";
	
	public static String readProperty(String key) throws Exception{
		Properties prop = new Properties();
		InputStream is = PropertyReader.class.getResourceAsStream(propertyPath);
		prop.load(is);
		return prop.getProperty(key);
	}
	
	public static String readJsonProperty(String fileName) throws Exception{
		String text = "";
		String fullFileName = jsonPropertyPath+"/"+fileName;
		text = new String(Files.readAllBytes(Paths.get(fullFileName)));
		return text;
	}
			
}
