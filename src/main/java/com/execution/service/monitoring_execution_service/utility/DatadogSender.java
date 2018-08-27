package com.execution.service.monitoring_execution_service.utility;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

public class DatadogSender implements NotificationSender {
	
	private static StatsDClient statsd;
	
	@Override
	public synchronized boolean sendNotification(String target, String subject, String text, String fileName, String filePath) throws Exception {
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
					    "my.prefix",                          /* prefix to any stats; may be null or empty string */
					    "10.0.1.253",                  /* common case: localhost */
					     8125
					     //new String[] {"tag:value"}  /* port */
					    /* Datadog extension: Constant tags, always applied */
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
	/*
	public static void main(String[] args){
		
		DatadogSender ds = new DatadogSender();
		
		try {
			boolean a = ds.sendNotification("{\"host\": \"10.0.1.253\", \"port\": 8125, \"prefix\": \"my.test\", \"gauge\": {\"name\": \"bar\", \"value\": 60000}}", "", "", "", "");
			System.out.println(a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	*/

}
