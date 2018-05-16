package com.execution.service.monitoring_execution_service.model;


import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
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
import com.execution.service.monitoring_execution_service.utility.EmailSender;
import com.execution.service.monitoring_execution_service.utility.NotificationSender;
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
			this.maxRow = 10000;
			this.path = "reports/unknown/";
		}
	}
	
	private List<List<CheckInfo>> fromCheckListToCheckBlocks(List<CheckInfo> checks){
		List<List<CheckInfo>> blocks = new ArrayList<List<CheckInfo>>();
		System.out.println("chceks size: "+checks.size());
		if(checks != null && checks.size() != 0){
			int i = 0;
			while(i < checks.size()){
				List<CheckInfo> block = new ArrayList<>();
				boolean findNextAnd = false;
				for(int j=i; j<checks.size(); j++){
					CheckInfo check = checks.get(j);
					System.out.println("check getCheckConjunctionType: "+check.getCheckConjunctionType());
					System.out.println("check isActive: "+check.getIsActive());
					if(check.getIsActive() 
							&& check.getCheckConjunctionType().equalsIgnoreCase("AND") 
							&& block.size() == 0){
						System.out.println("11");
						block.add(check);
					}
					else if(check.getIsActive() 
							&& check.getCheckConjunctionType().equalsIgnoreCase("OR") &&
							block.size() > 0){
						System.out.println("22");
						block.add(check);
					}
					else if(check.getIsActive()
							&&check.getCheckConjunctionType().equalsIgnoreCase("AND") &&
							block.size() >0 ){
						System.out.println("33");
						i = j;
						findNextAnd = true;
						break;
					}	
				}
				
				System.out.println("block size: "+ block.size());
				if(findNextAnd){
					if(block.size() > 0){
						blocks.add(block);
					}
				}
				else{
					if(block.size() > 0){
						blocks.add(block);
					}
					i++;
				}
			}
		}
		return blocks;
	}
	
	//we assume the checks are already sorted! It has been handled by db service!
	/*
	private List<List<CheckInfo>> fromCheckListToCheckBlocks(List<CheckInfo> checks){
		List<List<CheckInfo>> blocks = new ArrayList<List<CheckInfo>>();
		List<CheckInfo> block = new ArrayList<>();
		
		if(checks != null && checks.size() != 0){
			System.out.println("checks size: "+ checks.size());
			for(int i=0; i<checks.size(); i++){
				CheckInfo check = checks.get(i);
				if(check.isActive() && i< checks.size()-1){
					if(check.getCheckConjunctionType().equalsIgnoreCase("AND")){
						if(block.isEmpty() ){
							block.add(check);
						}
						else{
							blocks.add(block);
							block.clear();
							block.add(check);
						}
					}
					else if(check.getCheckConjunctionType().equalsIgnoreCase("OR")){
						block.add(check);
					}
				}
				else if(check.isActive() && i== checks.size()-1){
					if(!block.isEmpty()){
						blocks.add(block);
					}
				}
			}
		}
		return blocks;
	}
	*/
	
	private boolean isTrigger(ResultSet rs, List<CheckInfo> checks, Map<Integer, Integer> typeMap){
		if(checks == null || checks.size() == 0){
			System.out.println("no checks: ");
			return false;
		}
		boolean res = true;
		try{
			List<List<CheckInfo>> blocks = fromCheckListToCheckBlocks(checks);
			boolean[] blocksWindow = new boolean[blocks.size()];
			System.out.println("blocks size: "+blocks.size());
			for(int i=0; i<blocks.size(); i++){
				List<CheckInfo> checkList = blocks.get(i);
				int[] checksWindow = new int[checkList.size()];
				while(rs.next()){
					for(int j=0; j < checkList.size(); j++){
						CheckInfo check = checkList.get(j);
						if(check != null){
							//get column name
							int columnNum = -1;
							String columnName = null;
							for(Integer key: typeMap.keySet()){
								String columnAttributeName = rs.getMetaData().getColumnName(key);
								if(columnAttributeName.equalsIgnoreCase(check.getAttributeName())){
									columnNum = key;
									columnName = columnAttributeName;
								}
							}
							String value = TypeAdapter.fromResultSetToString(rs, columnNum, typeMap.get(columnNum));
							int isCheckTrigger = check.isTriggered(columnName, typeMap.get(columnNum), value);
							if(isCheckTrigger == 1){
								blocksWindow[i] = true;
								break;
							}
							else if(isCheckTrigger == 0){
								if(checksWindow[j] == 0 && rs.isLast()){
									blocksWindow[i] = true;
									break;
								}
							}
							else{
								checksWindow[j] = -1;
							}
						}
					}
					if(blocksWindow[i]){
						break;
					}
				}
				rs.beforeFirst();
			}
			for(int k = 0; k<blocksWindow.length; k++){
				if(!blocksWindow[k]){
					res = false;
					break;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	
	/*
	private isTrigger(ResultSet rs, List<CheckInfo> checks, Map<Integer, Integer> typeMap){
		if(checks == null || checks.size() == 0){
			return false;
		}
		boolean res = false;
		
		List<List<CheckInfo>> blocks = fromCheckListToCheckBlocks(checks);
		boolean[] blockWindow = new boolean[blocks.size()];
		for(int i=0; i<blocks.size(); i++){
			List<CheckInfo> block = blocks.get(i);
			while(rs.next()){
				for(Integer key: typeMap.keySet()){
					String columnName = rs.getMetaData().getColumnName(key);
					int[] window = new int[block.size()];
					for(int j=0; j<block.size(); j++){
						int isCheckTrigger = -1;
						CheckInfo check = block.get(j);
						if(check != null){
							if(columnName.equalsIgnoreCase(check.getAttributeName())){
								String value = TypeAdapter.fromResultSetToString(rs, key, typeMap.get(key));
								isCheckTrigger = check.isTriggered(columnName, typeMap.get(key), value);
							}
						}
						if(isCheckTrigger == 1){
							blockWindow[i]=true;
							break;
						}
						else if(isCheckTrigger == 0 ){
							if(window[j] == 0 && rs.isLast()){
								blockWindow[i]=true;
								break;
							}
						}
						else{
							window[j] = -1;
						}
						
					}

				}

			}
			rs.beforeFirst();
		}
		
	}
	*/
	
	private RuleResult executeRule(Connection conn, String title, String sql, String ruleType, List<CheckInfo> checks) throws Exception{
		//make resultset reusable
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
		
		
		if(ruleType.equalsIgnoreCase("Report")){
			System.out.println("This is a report!");
			rr.shouldNotify=true;

		}
		else if(ruleType.equalsIgnoreCase("Alert")){
			System.out.println("This is a alert!");
			rs.beforeFirst();
			if(isTrigger(rs, checks, typeMap)){
				rr.shouldNotify=true;
			}
		}
		
		return rr;
	}
	
	private void sendNotification(List<RecipientInfo> recipients,String ruleType, String fileName, String filePath) throws Exception{
		if(recipients == null || recipients.size()==0){
			return;
		}
		String subject = "Your Subscribed "+ruleType;
		String text = "Hi User,\n\nAttached is the result of your suscribed "+ruleType+" for your review.\n\nBest,\nChangshuo Gao\n";
		for(RecipientInfo recipient : recipients){
			if(recipient.getRecipientType().equalsIgnoreCase("Email")){
				NotificationSender ns = new EmailSender();
				boolean res = ns.sendNotification(recipient.getTarget(), subject, text, fileName, filePath);
				if(!res){
					System.out.println("Send email to target "+ recipient.getTarget()+" failed!");
				}
				else{
					System.out.println("Send email to target "+ recipient.getTarget()+" successed!");
				}
				
			}
			else if(recipient.getRecipientType().equalsIgnoreCase("Slack")){
				//TODO
			}			
		}
		
	}
	
	@Override
	public void run(){
		Connection connection = null;
		RuleResult ruleResult = null;
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

				if (connInfo != null && ruleStatus.equalsIgnoreCase("Active")) {
					int connId = connInfo.getId();
					// get connection for connection pool
					connection = connMap.getMap().get(connId).getConnection();
					ruleResult = executeRule(connection, ruleName, ruleContent, ruleType, ruleChecks);
					if(ruleResult !=null && ruleResult.excel != null){
						//generate directory if not exist
						File dir=new File(this.path);
						if(!dir.exists()){
							dir.mkdirs();
						}
						
						//put report under this directory
						String fullFileName = this.path+ruleResult.name;
						FileOutputStream outputStream = new FileOutputStream(fullFileName);
						ruleResult.excel.write(outputStream);
						
						//send message to notification service
						if(ruleResult.shouldNotify){
							sendNotification(ruleRecipients, ruleType, ruleResult.name, fullFileName);
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(ruleResult != null && ruleResult.excel != null){try {ruleResult.excel.close();} catch (Exception e) {e.printStackTrace();}}
			if(connection != null){try{connection.close();} catch (Exception e) {e.printStackTrace();}}
		}
	}

}
