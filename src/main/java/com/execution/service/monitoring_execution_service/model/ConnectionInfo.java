package com.execution.service.monitoring_execution_service.model;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionInfo {
	
	private int id;
	private String name;
	private String connectionType;
	private String host;
	private int port;
	private String db;
	private String username;
	private String password;
	private boolean isActive;
	
	
	private JDBCInfo toJDBCInfo () throws Exception{
		JDBCInfo jdbcInfo = new JDBCInfo();
		
		if(this.connectionType != null && this.port != 0 
				&& this.db != null && this.host != null 
				&& this.username != null && this.password != null){
			
			if(this.connectionType.equalsIgnoreCase("mysql")){
				jdbcInfo.setJdbcDriverClass("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://"+this.host+":"+String.valueOf(this.port)+"/"+this.db+"?useSSL=false";
				jdbcInfo.setJdbcUrl(url);
			}
			else if(this.connectionType.equalsIgnoreCase("postgresql")){
				jdbcInfo.setJdbcDriverClass("org.postgresql.Driver");
				String url = "jdbc:postgresql://"+this.host+":"+String.valueOf(this.port)+"/"+this.db;
				jdbcInfo.setJdbcUrl(url);
			}
			else if(this.connectionType.equalsIgnoreCase("redshift")){
				jdbcInfo.setJdbcDriverClass("com.amazon.redshift.jdbc.Driver");
				String url = "jdbc:redshift://"+this.host+":"+String.valueOf(this.port)+"/"+this.db;
				jdbcInfo.setJdbcUrl(url);
			}
			else {
				throw new Exception();
			}
			jdbcInfo.setJdbcUser(this.username);
			jdbcInfo.setJdbcPassword(this.password);
			
			return jdbcInfo;
		}
		else {
			throw new Exception();
		}
		
	}
	
	public void configJdbcConnection(ComboPooledDataSource cpds) throws Exception{
		JDBCInfo jdbcInfo = this.toJDBCInfo();
		
		cpds.setDriverClass(jdbcInfo.getJdbcDriverClass());
		cpds.setJdbcUrl(jdbcInfo.getJdbcUrl());
		cpds.setUser(jdbcInfo.getJdbcUser());
		cpds.setPassword(jdbcInfo.getJdbcPassword());
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getConnectionType() {
		return connectionType;
	}


	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public String getDb() {
		return db;
	}


	public void setDb(String db) {
		this.db = db;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public boolean getIsActive() {
		return isActive;
	}


	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	
}
