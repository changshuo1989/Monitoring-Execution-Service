package com.execution.service.monitoring_execution_service.utility;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.execution.service.monitoring_execution_service.model.RuleInfo;

public class DatabaseServiceClient {
	
	private static DatabaseServiceClient instance = null;
	
	protected DatabaseServiceClient(){
		
	}
	
	public static DatabaseServiceClient getInstance(){
		if(instance == null){
			synchronized (DatabaseServiceClient.class){
				if(instance == null){
					instance = new DatabaseServiceClient();
				}
			}
		}
		return instance;
	}
	
	
	public List<RuleInfo> getRuleInfoListFromDatabaseService(List<Integer> ruleIds) throws Exception{
		String uri = generateGetUrl(ruleIds);
		System.out.println("uri: "+uri);
		Client client = ClientBuilder.newClient();
		return client.target(uri).request(MediaType.APPLICATION_JSON).get(new GenericType<List<RuleInfo>>(){});
	}
	
	
	private String generateGetUrl(List<Integer> ruleIds) throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append(PropertyReader.readProperty("endpoint"));
		if(ruleIds != null && ruleIds.size()>0){
			for(int i=0; i<ruleIds.size(); i++){
				if(i == 0){
					sb.append(String.valueOf(ruleIds.get(i)));
				}
				else{
					sb.append(','+String.valueOf(ruleIds.get(i)));
				}
			}
		}
		return sb.toString();
	}
}
