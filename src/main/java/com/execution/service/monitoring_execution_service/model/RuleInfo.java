package com.execution.service.monitoring_execution_service.model;

import java.util.List;

public class RuleInfo {
	
	private int id;
	private String name;
	private String content;
	private String ruleStatus;
	private String ruleType;
	private ConnectionInfo connection;
	private List<CheckInfo> checks;
	private List<RecipientInfo> recipients;
	

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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRuleStatus() {
		return ruleStatus;
	}
	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public ConnectionInfo getConnection() {
		return connection;
	}
	public void setConnection(ConnectionInfo connection) {
		this.connection = connection;
	}
	public List<CheckInfo> getChecks() {
		return checks;
	}
	public void setChecks(List<CheckInfo> checks) {
		this.checks = checks;
	}
	public List<RecipientInfo> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<RecipientInfo> recipients) {
		this.recipients = recipients;
	}
	
	
	
}
