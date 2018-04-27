package com.execution.service.monitoring_execution_service.model;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.execution.service.monitoring_execution_service.utility.DateTimeAdapter;
import com.execution.service.monitoring_execution_service.utility.PropertyReader;
import com.execution.service.monitoring_execution_service.utility.TypeAdapter;


public class RuleExecution implements Runnable{

	public RuleInfo ruleInfo;
	public ConnectionPoolMap connMap;
	public int maxRow;
	public String path;
	
	public RuleExecution(RuleInfo ri, ConnectionPoolMap connMap) {
		this.ruleInfo = ri;
		this.connMap = connMap;
		try{
			this.maxRow = Integer.parseInt(PropertyReader.readProperty("max_row"));
			this.path = "reports/"+ri.getConnection().getName()+"/"+ri.getName()+"/";
		}
		catch(Exception e){
			this.maxRow = 5000;
		}
	}
	
	private boolean isTriggerAlert(ResultSet rs, List<CheckInfo> checks) throws Exception{
		
		//TODO for test now, implement this
		if(checks == null || checks.size() == 0){
			return true;
		}
		while(rs.next()){
			for (int i=0; i<checks.size(); i++){
				/*
				int seq = checks.get(i).getSequence();
				String benchmark = checks.get(i).getBenchmark();
				String benchmarkType = checks.get(i).getCheckBenchmarkType();
				String attributeName = checks.get(i).getAttributeName();
				boolean isActive = checks.get(i).isActive();
				String checkConjunctionType = checks.get(i).getCheckConjunctionType();
				String checkLogicType = checks.get(i).getCheckLogicType();
				String checkOperatorType = checks.get(i).getCheckOperatorType();
				*/
				
			}
			
		}
		return true;
		
		
	}
	
	
	
	private RuleResult executeRule(Connection conn, String title, String sql, String ruleType, List<CheckInfo> checks) throws Exception{
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		RuleResult rr = new RuleResult();
		rr.name=title+"_"+DateTimeAdapter.fromDateTimeToTitleString(new Date())+".xlsx";
		//init workbook and sheet
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(ruleType);
		
		
		//form header
		ResultSetMetaData rsmd = rs.getMetaData();
		XSSFRow headerRow = sheet.createRow(0);
		Map<Integer, Integer> typeMap = new LinkedHashMap<>();
		//CellStyle style = workbook.createCellStyle();
	    //style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
	   
		for (int i = 1; i <= rsmd.getColumnCount(); i++){
			typeMap.put(i, rsmd.getColumnType(i));
			String headerName=rsmd.getColumnName(i);
			XSSFCell cell = headerRow.createCell(i-1);
			cell.setCellValue(headerName);
			
		}
		//form content
		int rowCount = 1;
		
		if(ruleType.equals("Report")){
			rr.shouldNotify=true;
			while (rs.next()) {
				Set<Integer> keys = typeMap.keySet();
				XSSFRow bodyRow = sheet.createRow(rowCount);
				int cellCount = 0;
				for (Integer key : keys) {
					XSSFCell cell = bodyRow.createCell(cellCount);
					String value = TypeAdapter.fromResultSetToString(rs, key, typeMap.get(key));
					cell.setCellValue(value);
					cellCount++;
				}
				rowCount++;
			}
			rr.excel=workbook;
		}
		else if(ruleType.equals("Alert")){
			while (rs.next()){
				Set<Integer> keys = typeMap.keySet();
				XSSFRow bodyRow = sheet.createRow(rowCount);
				int cellCount = 0;
				for (Integer key : keys) {
					XSSFCell cell = bodyRow.createCell(cellCount);
					//TODO
					
				}
			}
		}
		
		
		return rr;
	}
	
	
	@Override
	public void run(){
		Connection connection = null;
		try {
			if (ruleInfo != null) {
				// get all fields
				int ruleId = ruleInfo.getId();
				String ruleName = ruleInfo.getName();
				String ruleContent = ruleInfo.getContent();
				String ruleStatus = ruleInfo.getRuleStatus();
				String ruleType = ruleInfo.getRuleType();
				List<CheckInfo> ruleChecks = ruleInfo.getChecks();
				List<RecipientInfo> ruleRecipients = ruleInfo.getRecipients();
				ConnectionInfo connInfo = ruleInfo.getConnection();

				if (connInfo != null) {
					int connId = connInfo.getId();
					// get connection for connection pool
					connection = connMap.getMap().get(connId).getConnection();
					RuleResult ruleResult = executeRule(connection, ruleName, ruleContent, ruleType, ruleChecks);
					if(ruleResult !=null && ruleResult.excel != null){
						//generate directory if not exist
						File dir=new File(this.path);
						if(!dir.exists()){
							dir.mkdirs();
						}
						
						//put report under this directory
						
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}
