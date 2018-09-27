package com.execution.service.monitoring_execution_service.utility;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class DatadogSender implements NotificationSender {
	
	private String api_key;
	
	public DatadogSender() throws Exception{
		this.api_key = PropertyReader.readProperty("api_key");
	}
	
	
	@Override
	public boolean sendNotification(String target, String subject, String text, String fileName, String filePath)
			throws Exception {
		// TODO Auto-generated method stu
		boolean isValid = JsonValidator.validateJsonSchema(PropertyReader.readJsonProperty("DatadogAPI.json"), target);
		boolean res = false;
		
		if(isValid && this.api_key != null && this.api_key.length() != 0){
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(target);
			//String apiKey = jsonNode.get("api_key").asText();
			String gaugeName = jsonNode.get("gauge").get("name").asText();
			//System.out.println("gauge name :"+ gaugeName);
			double gaugeValue = jsonNode.get("gauge").get("value").asDouble();
			//long gaugeValue = jsonNode.get("gauge").get("value").asLong();
			//System.out.println("gauge value:" + gaugeValue);
			String host = "";
			if(jsonNode.get("host") != null){
				host = jsonNode.get("host").asText();
			}
			//System.out.println("host:" + host);
			List<String> tagList = null;
			if(jsonNode.get("tags") != null){
				if(jsonNode.get("tags").isArray()){
					tagList = new ArrayList<>();
					for(JsonNode objNode : jsonNode.get("tags")){
						tagList.add(objNode.asText());
					}
				}
			}
			if(tagList != null){
				for(String s :tagList ){
					//System.out.println("tag:" + s);
				}
			}
			ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
			jsonObject.put("metric", gaugeName);
			
			//jsonObject.put("points", gaugeValue);
			ArrayNode point = mapper.createArrayNode();
			point.add(System.currentTimeMillis() / 1000L);
			point.add(gaugeValue);
			
			ArrayNode points = mapper.createArrayNode();
			points.add(point);
			jsonObject.putArray("points").addAll(points);
			
			if(!host.equals("")){
				jsonObject.put("host", host);
			}
			if(tagList != null && tagList.size() != 0){
				ArrayNode tags = mapper.valueToTree(tagList);
				jsonObject.putArray("tags").addAll(tags);
			}
			ArrayNode series = mapper.createArrayNode();
			series.add(jsonObject);
			ObjectNode bodyJosn = JsonNodeFactory.instance.objectNode();
			bodyJosn.putArray("series").addAll(series);
			//System.out.println(bodyJosn.toString());
			if(sendPostRequest(this.api_key, bodyJosn.toString())){
				return true;
			}
			
		}
		
		return false;
	}
	
	
	public boolean sendPostRequest(String apiKey, String body) throws Exception{
		
		boolean res = false;
		String urlStr = "https://api.datadoghq.com/api/v1/series?api_key="+apiKey;
		URL url = new URL(urlStr);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		//add header
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-type", "application/json");
		
		//send request
		con.setDoOutput(true);
		OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
		wr.write(body);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		//System.out.println(responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());
		if(responseCode >= 200 && responseCode <= 299){
			res = true;
		}
		return res;
	}
	/*
	public synchronized boolean sendNotificationOld(String target, String subject, String text, String fileName, String filePath) throws Exception {
		//validate json string
		boolean isValid = JsonValidator.validateJsonSchema(PropertyReader.readJsonProperty("Datadog.json"), target);
		boolean res = false;

		if(isValid){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(target);
			String host = jsonNode.get("host").asText();
			System.out.println("host: "+host);
			int port = jsonNode.get("port").asInt();
			System.out.println("port: "+ port);
			String prefix = jsonNode.get("prefix").asText();
			System.out.println("prefix: " + prefix);
			String gaugeName = jsonNode.get("gauge").get("name").asText();
			System.out.println("gauge name :"+ gaugeName);
			//double gaugeValue = jsonNode.get("gauge").get("value").asDouble();
			long gaugeValue = jsonNode.get("gauge").get("value").asLong();
			System.out.println("gauge value:" + gaugeValue);
			
			statsd = new NonBlockingStatsDClient(
					    "my.prefix",                          
					    "10.0.1.253",                  
					     //new String[] {"tag:value"} 
			);
			//prepare send metric
			//StatsDClient statsd = new NonBlockingStatsDClient(prefix, host, port);
			statsd.recordGaugeValue(gaugeName, gaugeValue);
			//statsd.recordGaugeValue(gaugeName+"_count", 1.0);
			statsd.close();
			res=true;
		}
		
		return res;
	}
	*/
	/*
	public static void main(String[] args){
		
		
		
		try {
			DatadogSender ds = new DatadogSender();
			//boolean a = ds.sendNotification("{\"host\": \"10.0.1.253\", \"port\": 8125, \"prefix\": \"my.test\", \"gauge\": {\"name\": \"bar\", \"value\": 10000}}", "", "", "", "");
			boolean a = ds.sendNotification("{\"host\": \"10.0.1.253\", \"gauge\": {\"name\": \"test.bar\", \"value\": 10000}, \"tags\":[\"environment:test\", \"adsfsdfa\"]}", "", "", "", "");
			System.out.println(a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	*/
	
}
