package com.execution.service.monitoring_execution_service.utility;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
	private static String propertyPath="/application.properties";
	
	public static String readProperty(String key) throws Exception{
		Properties prop = new Properties();
		InputStream is = PropertyReader.class.getResourceAsStream(propertyPath);
		prop.load(is);
		return prop.getProperty(key);
	}
			
}
