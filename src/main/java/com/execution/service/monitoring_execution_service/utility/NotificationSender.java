package com.execution.service.monitoring_execution_service.utility;

public interface NotificationSender {
	
	public boolean sendNotification(String target, String subject, String text, String fileName, String filePath) ;
}
