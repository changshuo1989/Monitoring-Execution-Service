package com.execution.service.monitoring_execution_service.model;

import org.apache.poi.ss.usermodel.Workbook;

public class RuleResult {
	String name;
	Workbook excel;
	boolean shouldNotify;
}
