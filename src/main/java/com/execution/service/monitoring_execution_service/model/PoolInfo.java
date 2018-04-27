 	package com.execution.service.monitoring_execution_service.model;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class PoolInfo {
	private int minPoolSize;
	private int acquireIncrement;
	private int maxPoolSize;
	
	
	public PoolInfo(){
		this.minPoolSize=3;
		this.acquireIncrement=3;
		this.maxPoolSize=30;
	}

	//TODO add more configration variables here
	public void configConnectionPool(ComboPooledDataSource cpds){
		cpds.setMinPoolSize(this.minPoolSize);
		cpds.setAcquireIncrement(this.acquireIncrement);
		cpds.setMaxPoolSize(this.maxPoolSize);
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
	
}
