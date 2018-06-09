package com.execution.service.monitoring_execution_service.model;

public class MetaData {
	int order;
	String name;
	int sqlType;
	
	public MetaData(int order, String name, int sqlType) {
		super();
		this.order = order;
		this.name = name;
		this.sqlType = sqlType;
	}
	
	
}
