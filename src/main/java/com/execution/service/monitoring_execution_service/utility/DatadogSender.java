package com.execution.service.monitoring_execution_service.utility;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

public class DatadogSender implements NotificationSender {

	@Override
	public boolean sendNotification(String target, String subject, String text, String fileName, String filePath) throws Exception {
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
			System.out.println("prefix:" + prefix);
			String gaugeName = jsonNode.get("gauge").get("name").asText();
			System.out.println("gauge name:"+ gaugeName);
			double gaugeValue = jsonNode.get("gauge").get("value").asDouble();
			System.out.println("gauge value:" + gaugeValue);
			
			//prepare send metric
			StatsDClient statsd = new NonBlockingStatsDClient(prefix, host, port, new String[] {"tag:value"} );
			statsd.incrementCounter(gaugeName);
			statsd.recordGaugeValue(gaugeName, gaugeValue);
			statsd.close();
			res=true;
		}
		
		return res;
	}

}
