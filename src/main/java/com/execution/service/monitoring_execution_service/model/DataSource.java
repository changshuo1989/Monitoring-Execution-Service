package com.execution.service.monitoring_execution_service.model;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DataSource {

	private ConnectionInfo connectionInfo;
	private PoolInfo poolInfo;
	private ComboPooledDataSource pooledSource;
	
	public DataSource(ConnectionInfo connInfo) throws Exception, IOException, SQLException, PropertyVetoException{
		if(connInfo == null){
			throw new NullPointerException();
		}
		this.connectionInfo = connInfo;
		this.poolInfo = new PoolInfo();
			
		this.pooledSource = new ComboPooledDataSource();			
		//add connection details
		this.connectionInfo.configJdbcConnection(this.pooledSource);
		//config pool variables
		this.poolInfo.configConnectionPool(this.pooledSource);
		
	}
	
	public DataSource(ConnectionInfo connInfo, PoolInfo poolInfo) throws Exception, IOException, SQLException, PropertyVetoException{
		if(connInfo == null || poolInfo == null){
			throw new NullPointerException();
		}
		
		this.connectionInfo = connInfo;
		this.poolInfo = poolInfo;
		
		this.pooledSource = new ComboPooledDataSource();			
		//add connection details
		this.connectionInfo.configJdbcConnection(this.pooledSource);
		//config pool variables
		this.poolInfo.configConnectionPool(this.pooledSource);
		
	}
	
	
	public Connection getConnection() throws SQLException{
		
		return this.pooledSource.getConnection();
	}
	
	
	public void closePooledDataSource(){
		this.pooledSource.close();
	}
	
	
	
}
