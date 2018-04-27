package com.execution.service.monitoring_execution_service.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectionPoolMap {
	private static ConnectionPoolMap instance = null;
	
	private Map<Integer, DataSource> map;
	
	
	protected ConnectionPoolMap(){
		this.map = new ConcurrentHashMap<>();
	}
	
	public static ConnectionPoolMap getInstance(){
		if(instance ==  null){
			synchronized (ConnectionPoolMap.class){
				if(instance == null){
					instance = new ConnectionPoolMap();
				}
			}
		}
		return instance;
	}
	//TODO add useful connection functions

	public Map<Integer, DataSource> getMap() {
		return map;
	}

	public void setMap(Map<Integer, DataSource> map) {
		this.map = map;
	}
	
}
