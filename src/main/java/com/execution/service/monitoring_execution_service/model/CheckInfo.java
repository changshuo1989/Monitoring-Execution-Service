package com.execution.service.monitoring_execution_service.model;

import java.sql.ResultSet;

public class CheckInfo {
	
	private int id;
	private int sequence;
	private String benchmark;
	private String checkBenchmarkType;
	private String attributeName;
	private boolean isActive;
	private String checkConjunctionType;
	private String checkLogicType;
	private String checkOperatorType;
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getBenchmark() {
		return benchmark;
	}
	public void setBenchmark(String benchmark) {
		this.benchmark = benchmark;
	}
	public String getCheckBenchmarkType() {
		return checkBenchmarkType;
	}
	public void setCheckBenchmarkType(String checkBenchmarkType) {
		this.checkBenchmarkType = checkBenchmarkType;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getCheckConjunctionType() {
		return checkConjunctionType;
	}
	public void setCheckConjunctionType(String checkConjunctionType) {
		this.checkConjunctionType = checkConjunctionType;
	}
	public String getCheckLogicType() {
		return checkLogicType;
	}
	public void setCheckLogicType(String checkLogicType) {
		this.checkLogicType = checkLogicType;
	}
	public String getCheckOperatorType() {
		return checkOperatorType;
	}
	public void setCheckOperatorType(String checkOperatorType) {
		this.checkOperatorType = checkOperatorType;
	}
	/*
	public boolean isTriggered(ResultSet rs){
		//if(this.checkBenchmarkType)
		//TODO implement this
	}
	*/
	
	
}
