 	package com.execution.service.monitoring_execution_service.model;

import com.execution.service.monitoring_execution_service.utility.PropertyReader;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class PoolInfo {
	private int minPoolSize=2;
	private int acquireIncrement=2;
	private int maxPoolSize=20;
	private int maxIdleTime=240;
	private boolean testConnectionOnCheckout=true;
	private String preferredTestQuery = "select 1";
	
	
	
	public PoolInfo(){
		/*
		this.minPoolSize=2;
		this.acquireIncrement=2;
		this.maxPoolSize=20;
		this.maxIdleTime=240;
		*/
		try{
			String minPoolSizeStr = PropertyReader.readProperty("minPoolSize");
			String acquireIncrementStr =  PropertyReader.readProperty("acquireIncrement");
			String maxPoolSizeStr = PropertyReader.readProperty("maxPoolSize");
			String maxIdleTimeStr = PropertyReader.readProperty("maxIdleTime");
			String testConnectionOnCheckout = PropertyReader.readProperty("TestConnectionOnCheckout");
			String preferredTestQueryStr = PropertyReader.readProperty("PreferredTestQuery");
			
			this.minPoolSize = Integer.parseInt(minPoolSizeStr);
			this.acquireIncrement = Integer.parseInt(acquireIncrementStr);
			this.maxPoolSize = Integer.parseInt(maxPoolSizeStr);
			this.maxIdleTime = Integer.parseInt(maxIdleTimeStr);
			this.testConnectionOnCheckout = Boolean.parseBoolean(testConnectionOnCheckout);
			this.preferredTestQuery = preferredTestQueryStr;
					
		}
		catch(Exception e){
			System.out.println("cannot parse connection pool configration!");
		}
	}

	//TODO add more configration variables here
	public void configConnectionPool(ComboPooledDataSource cpds){
		cpds.setMinPoolSize(this.minPoolSize);
		cpds.setAcquireIncrement(this.acquireIncrement);
		cpds.setMaxPoolSize(this.maxPoolSize);
		cpds.setMaxIdleTime(this.maxIdleTime);
		cpds.setTestConnectionOnCheckout(this.testConnectionOnCheckout);
		cpds.setPreferredTestQuery(this.preferredTestQuery);
	}

	
	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public int getAcquireIncrement() {
		return acquireIncrement;
	}

	public void setAcquireIncrement(int acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public boolean isTestConnectionOnCheckout() {
		return testConnectionOnCheckout;
	}

	public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
		this.testConnectionOnCheckout = testConnectionOnCheckout;
	}

	public String getPreferredTestQuery() {
		return preferredTestQuery;
	}

	public void setPreferredTestQuery(String preferredTestQuery) {
		this.preferredTestQuery = preferredTestQuery;
	}
	
	
}
