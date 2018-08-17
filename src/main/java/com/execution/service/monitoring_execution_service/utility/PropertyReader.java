package com.execution.service.monitoring_execution_service.utility;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


public class PropertyReader {
	private static String propertyPath="/application.properties";
	private static String jsonPropertyPath = "/json";
	
	public static String readProperty(String key) throws Exception{
		Properties prop = new Properties();
		InputStream is = PropertyReader.class.getResourceAsStream(propertyPath);
		prop.load(is);
		return prop.getProperty(key);
	}
	
	public static String readJsonProperty(String fileName) throws Exception{
		String fullFileName = jsonPropertyPath+"/"+fileName;
		return new String(Files.readAllBytes(Paths.get(PropertyReader.class.getResource(fullFileName).toURI())));
		
		
	}
	
	/*
	public static void main (String[] args){
		try {
			System.out.println(readJsonProperty("Datadog.json"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
			
}
