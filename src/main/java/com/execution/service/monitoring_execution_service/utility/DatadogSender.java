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
		if(isValid){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(target);
			String host = jsonNode.get("host").asText();
			int port = jsonNode.get("port").asInt();
			String prefix = jsonNode.get("prefix").asText();
			String gaugeName = jsonNode.get("name").asText();
			double gaugeValue = jsonNode.get("value").asDouble();
			
			//prepare send metric
			StatsDClient statsd = new NonBlockingStatsDClient(prefix, host, port);
			statsd.recordGaugeValue(gaugeName, gaugeValue);
		}
		
		return false;
	}

}
